-- -- DEFINE setting can be changed to allow &'s (ampersands) to be used in text
SET DEFINE ~

CREATE PACKAGE EXAM_PACK
AS
   PROCEDURE pr_process_effort (new_pr_id NUMBER);
END exam_pack;

/
CREATE PACKAGE PKG_PROJECT_STAGE
AS
   /* Get a list of projects that closed betwwen start_date and end_date */
   -- Date format dd-MON-yy
   FUNCTION getprojects (in_start_date IN VARCHAR2, end_date IN VARCHAR2)
      RETURN project_record_set PIPELINED;

   FUNCTION getprojectloc (in_start_date IN VARCHAR2, end_date IN VARCHAR2)
      RETURN project_record_set PIPELINED;

   /* Get project's milestones */
   FUNCTION getmilestones (in_project_id IN NUMBER, in_start_date IN DATE)
      RETURN milestone_record_set PIPELINED;

   /* Get timesheets base on project milestones that closed betwwen start_date and end_date */
   -- Date format dd-MON-yy
   FUNCTION gettimesheets (in_start_date   IN   VARCHAR2,in_end_date     IN   VARCHAR2,cat IN   NUMBER)
      RETURN timesheet_record_set PIPELINED;

   /* Get defects base on project milestones that closed betwwen start_date and end_date */
   -- Date format dd-MON-yy
   FUNCTION getdefects (in_start_date   IN   VARCHAR2,in_end_date     IN   VARCHAR2,cat IN   NUMBER)
      RETURN defect_record_set PIPELINED;

   /* Get modules of projects that closed betwwen start_date and end_date */
   -- Date format dd-MON-yy
   FUNCTION getmodules (in_start_date   IN   VARCHAR2,in_end_date     IN   VARCHAR2,cat IN   NUMBER)
      RETURN module_record_set PIPELINED;

   FUNCTION getmodules_ongoing (in_start_date   IN   VARCHAR2, in_end_date     IN   VARCHAR2, cat IN   NUMBER)
      RETURN module_record_set PIPELINED;

   FUNCTION getmodules_deliverable (in_start_date   IN   VARCHAR2, in_end_date IN   VARCHAR2 )
      RETURN deliverable_record_set PIPELINED;

   /* Get metric of projects that running betwwen start_date and end_date */
   -- Date format dd-MON-yy
   FUNCTION getmetric (in_start_date     IN   VARCHAR2,in_end_date       IN   VARCHAR2, in_metric_index   IN   NUMBER)
      RETURN deliverable_record_set PIPELINED;

   /* Get defects of project that closed betwwen start_date and end_date */
   -- Date format dd-MON-yy
   FUNCTION getdefectsdist (in_start_date   IN   VARCHAR2, in_end_date     IN   VARCHAR2,cat IN   NUMBER)
      RETURN defect_record_set PIPELINED;
END pkg_project_stage;
/
CREATE PACKAGE BODY EXAM_PACK
AS

/* -------------------------------------------------------------------------- */
/* Procedure to multiply data from table "process_effort" based on new        */
/* project_id                                                                 */
/* -------------------------------------------------------------------------- */
   PROCEDURE pr_process_effort (new_pr_id NUMBER)
   IS
      CURSOR cur_pr_id
      IS
         SELECT process_effort_id
           FROM process_effort
          WHERE project_id = 265;

      p_id      NUMBER;
      proc_id   NUMBER;
   BEGIN
      SELECT MAX (process_effort_id)
        INTO proc_id
        FROM process_effort;

      OPEN cur_pr_id;

      LOOP
         proc_id :=   proc_id
                    + 1;
         FETCH cur_pr_id INTO p_id;
         EXIT WHEN cur_pr_id%NOTFOUND;

         INSERT INTO process_effort
                     (process_effort_id, process_id, plan_effort,
                      re_plan_effort, project_id)
            SELECT proc_id, process_id, plan_effort, re_plan_effort, new_pr_id
              FROM process_effort
             WHERE process_effort_id = p_id;
      END LOOP;

      CLOSE cur_pr_id;
   END;
END exam_pack;

/
CREATE PACKAGE BODY PKG_PROJECT_STAGE
AS
   /* Get a list of projects that closed betwwen start_date and end_date */
   FUNCTION getprojects (in_start_date IN VARCHAR2, end_date IN VARCHAR2)
      -- Date format dd-MON-yyyy
   RETURN project_record_set PIPELINED
   IS
      CURSOR c
      IS
         SELECT   project_id, code, lifecycle.NAME,
                  NVL (plan_start_date, start_date) plan_start_date,
                  actual_finish_date, group_name
             FROM project, lifecycle
            WHERE status = 1
              AND project.CATEGORY = lifecycle.lifecycle_id
              AND actual_finish_date BETWEEN TO_DATE (TO_CHAR (in_start_date), 'dd-mon-yy')
                            AND TO_DATE (TO_CHAR (end_date), 'dd-mon-yy')
         ORDER BY code;
   BEGIN
      FOR rec IN c
      LOOP
         PIPE ROW (project_record (rec.project_id,
                                   rec.code,
                                   rec.NAME,
                                   NULL,
                                   rec.plan_start_date,
                                   rec.group_name
                                  ));
      END LOOP;

      RETURN;
   END;

   /* Get a list of projects with actual effort */
   FUNCTION getprojectloc (in_start_date IN VARCHAR2, end_date IN VARCHAR2)
      -- Date format dd-MON-yyyy
   RETURN project_record_set PIPELINED
   IS
      CURSOR c
      IS
         SELECT   project_id, code, lifecycle.NAME,
                  NVL (plan_start_date, start_date) plan_start_date,
                  actual_finish_date, group_name
             FROM project, lifecycle
            WHERE status = 1
              AND project.CATEGORY = lifecycle.lifecycle_id
              AND actual_finish_date BETWEEN TO_DATE (TO_CHAR (in_start_date),
                                                      'dd-mon-yy'
                                                     )
                                         AND TO_DATE (TO_CHAR (end_date),
                                                      'dd-mon-yy'
                                                     )
         ORDER BY code;

      var_project_id   NUMBER;
      var_actual_loc   NUMBER;

      CURSOR b
      IS
         SELECT SUM (a.actual_size) size_in_loc
           FROM module a
          WHERE a.project_id = var_project_id
            AND a.wp_id = 9                                -- Software package
            AND a.actual_size_type = 0;                           -- Languages
   BEGIN
      FOR rec IN c
      LOOP
         var_project_id := rec.project_id;

         OPEN b;

         FETCH b
          INTO var_actual_loc;

         CLOSE b;

         PIPE ROW (project_record (rec.project_id,
                                   rec.code,
                                   rec.NAME,
                                   var_actual_loc,
                                   rec.plan_start_date,
                                   rec.group_name
                                  ));
      END LOOP;

      RETURN;
   END;

   /* Get project's milestones */
   FUNCTION getmilestones (in_project_id IN NUMBER, in_start_date IN DATE)
      RETURN milestone_record_set PIPELINED
   IS
      CURSOR c
      IS
         SELECT   project_id, milestone_id, NAME, actual_finish_date
             FROM milestone
            WHERE project_id = in_project_id
         ORDER BY actual_finish_date;

      start_date   DATE        := in_start_date;
      rec          c%ROWTYPE;
      m_no         NUMBER;
   BEGIN
      m_no := 1;

      FOR rec IN c
      LOOP
         PIPE ROW (milestone_record (rec.project_id,
                                     rec.milestone_id,
                                     rec.NAME,
                                     rec.actual_finish_date,
                                     start_date,
                                     m_no
                                    ));
         start_date := rec.actual_finish_date;
         m_no := m_no + 1;
      END LOOP;

      RETURN;
   END;

   /* Get timesheets base on project milestones that closed betwwen start_date and end_date */
   FUNCTION gettimesheets (
      in_start_date   IN   VARCHAR2,
      in_end_date     IN   VARCHAR2,
      cat             IN   NUMBER
   )
      -- Date format dd-MON-yyyy
   RETURN timesheet_record_set PIPELINED
   IS
      CURSOR c_project
      IS
         SELECT   project_id, code,
                  NVL (plan_start_date, start_date) start_date,
                  actual_finish_date,
                  group_name
             FROM project
            WHERE status = 1
--                 AND category=cat
              AND actual_finish_date
                   BETWEEN TO_DATE (TO_CHAR (in_start_date),
                                                      'dd-mon-yy'
                                                     )
                                         AND TO_DATE (TO_CHAR (in_end_date),
                                                      'dd-mon-yy'
                                                     )
         ORDER BY code;

      rec_project                  c_project%ROWTYPE;

      CURSOR c_milestone
      IS
         SELECT   project_id, milestone_id, NAME, actual_finish_date -- base_finish_date
             FROM milestone
            WHERE project_id = rec_project.project_id
         ORDER BY actual_finish_date; -- base_finish_date;

      CURSOR c_count
      IS
         SELECT COUNT (milestone_id) mno
           FROM milestone
          WHERE project_id = rec_project.project_id;

      rec_milestone                c_milestone%ROWTYPE;
      milestone_no                 NUMBER;                  -- Milestone index
      mno                          NUMBER;            -- Numbers of milestones
      milestone_start_date         DATE;
      milestone_actual_finish_date   DATE;
--      milestone_base_finish_date   DATE;

      CURSOR c_timesheet
      IS
         SELECT occur_date, DURATION, process_id, wp_id
           FROM timesheet
          WHERE status = 4
            AND project_id = rec_project.project_id
            AND occur_date >= milestone_start_date
            AND occur_date < milestone_actual_finish_date;
                                             --rec_milestone.actual_finish_date;

      rec_timesheet                c_timesheet%ROWTYPE;
   BEGIN
      OPEN c_project;

      LOOP
         FETCH c_project
          INTO rec_project;

         EXIT WHEN c_project%NOTFOUND;
         milestone_start_date := rec_project.start_date;

         -- Get numbers of milestone
         OPEN c_count;

         FETCH c_count
          INTO mno;

         CLOSE c_count;

         milestone_no := 1;

         OPEN c_milestone;

         LOOP
            FETCH c_milestone
             INTO rec_milestone;

            EXIT WHEN c_milestone%NOTFOUND;
            milestone_actual_finish_date := rec_milestone.actual_finish_date + 1;
            dbms_output.put_line(milestone_actual_finish_date);
            IF c_milestone%ROWCOUNT = 1
            THEN
               -- Move start date to very early date for first milestone
               milestone_start_date := TO_DATE ('01-JAN-2000', 'DD-MON-YYYY');
            ELSIF c_milestone%ROWCOUNT = mno
            THEN
               -- Move finish date to very late date for last milestone
               milestone_actual_finish_date :=
                                       TO_DATE ('01-JAN-3000', 'DD-MON-YYYY');
            END IF;

            OPEN c_timesheet;

            LOOP
               FETCH c_timesheet
                INTO rec_timesheet;

               EXIT WHEN c_timesheet%NOTFOUND;
               PIPE ROW (timesheet_record (rec_project.project_id,
                                           rec_project.code,
                                           rec_timesheet.occur_date,
                                           rec_timesheet.DURATION,
                                           rec_timesheet.process_id,
                                           milestone_no,
                                           rec_timesheet.wp_id,
                                           rec_project.group_name,
                                           rec_project.group_name
                                          ));
            END LOOP;

            CLOSE c_timesheet;

            milestone_start_date := rec_milestone.actual_finish_date;
            milestone_no := milestone_no + 1;
         END LOOP;

         CLOSE c_milestone;
      END LOOP;

      CLOSE c_project;

      RETURN;
   END;

   /* Get defects base on project milestones that closed betwwen start_date and end_date */
   FUNCTION getdefects (
      in_start_date   IN   VARCHAR2,
      in_end_date     IN   VARCHAR2,
      cat             IN   NUMBER
   )
      -- Date format dd-MON-yyyy
   RETURN defect_record_set PIPELINED
   IS
      CURSOR c_project
      IS
         SELECT   project_id, code,
                  NVL (plan_start_date, start_date) start_date,
                  actual_finish_date,
                  group_name
             FROM project
            WHERE status = 1                                     --in (1,0,3)
--code in ('CityIIe2', 'DWH-T2C Tool', 'FCRM2-Maint', 'RTD')
/*                 AND category=cat*/
              AND NVL (actual_finish_date,
                       NVL (plan_finish_date, actual_finish_date)
                      ) BETWEEN TO_DATE (TO_CHAR (in_start_date),
                                                      'dd-mon-yy'
                                                     )
                                         AND TO_DATE (TO_CHAR (in_end_date),
                                                      'dd-mon-yy'
                                                     )
         ORDER BY code;

      rec_project                  c_project%ROWTYPE;

      CURSOR c_milestone
      IS
         SELECT   project_id, milestone_id, NAME, actual_finish_date
             FROM milestone
            WHERE project_id = rec_project.project_id
         ORDER BY actual_finish_date;

      CURSOR c_count
      IS
         SELECT COUNT (milestone_id) mno
           FROM milestone
          WHERE project_id = rec_project.project_id;

      rec_milestone                c_milestone%ROWTYPE;
      milestone_no                 NUMBER;                  -- Milestone index
      mno                          NUMBER;            -- Numbers of milestones
      milestone_start_date         DATE;
      milestone_actual_finish_date   DATE;

      CURSOR c_defect
      IS
         SELECT create_date, qa_id, process_id, defs_id AS severity_id, wp_id
           FROM defect
          WHERE ds_id <> 6
            AND project_id = rec_project.project_id
            AND create_date >= milestone_start_date
            AND create_date < milestone_actual_finish_date;

      rec_defect                   c_defect%ROWTYPE;
      weighted                     NUMBER;
   BEGIN
      OPEN c_project;

      LOOP
         FETCH c_project
          INTO rec_project;

         EXIT WHEN c_project%NOTFOUND;
         milestone_start_date := rec_project.start_date;

         -- Get numbers of milestone
         OPEN c_count;

         FETCH c_count
          INTO mno;

         CLOSE c_count;

         milestone_no := 1;

         OPEN c_milestone;

         LOOP
            FETCH c_milestone
             INTO rec_milestone;

            EXIT WHEN c_milestone%NOTFOUND;
            milestone_actual_finish_date := rec_milestone.actual_finish_date;

            IF c_milestone%ROWCOUNT = 1
            THEN
               -- Move start date to very early date for first milestone
               milestone_start_date := TO_DATE ('01-JAN-2000', 'DD-MON-YYYY');
            ELSIF c_milestone%ROWCOUNT = mno
            THEN
               -- Move finish date to very late date for last milestone
               milestone_actual_finish_date :=
                                       TO_DATE ('01-JAN-3000', 'DD-MON-YYYY');
            END IF;

            OPEN c_defect;

            LOOP
               FETCH c_defect
                INTO rec_defect;

               EXIT WHEN c_defect%NOTFOUND;

               IF rec_defect.severity_id = 1
               THEN                                                  -- Fatal
                  weighted := 10;
               ELSIF rec_defect.severity_id = 2
               THEN
                  weighted := 5;
               ELSIF rec_defect.severity_id = 3
               THEN
                  weighted := 3;
               ELSE
                  weighted := 1;
               END IF;

               PIPE ROW (defect_record (rec_project.project_id,
                                        rec_project.code,
                                        rec_defect.create_date,
                                        rec_defect.qa_id,
                                        rec_defect.process_id,
                                        rec_defect.wp_id,
                                        weighted,
                                        milestone_no,
                                        rec_project.group_name,
                                        rec_project.group_name
                                       ));
            END LOOP;

            CLOSE c_defect;

            milestone_start_date := rec_milestone.actual_finish_date;
            milestone_no := milestone_no + 1;
         END LOOP;

         CLOSE c_milestone;
      END LOOP;

      CLOSE c_project;

      RETURN;
   END;

   /* Get modules of projects that closed betwwen start_date and end_date */
   -- Date format dd-MON-yy
   FUNCTION getmodules (
      in_start_date   IN   VARCHAR2,
      in_end_date     IN   VARCHAR2,
      cat             IN   NUMBER
   )
      RETURN module_record_set PIPELINED
   IS
      CURSOR c_module
      IS
         SELECT p.project_id, p.code, p.group_name, p.CATEGORY, m.actual_size, m.REUSE,
                actual_size_unit_id, actual_size_type, wp_id, m.note
           FROM project p, module m
          WHERE p.project_id = m.project_id
--                 AND category=cat
            AND p.status = 1                                     -- in (0,1,3)
            AND actual_finish_date
                      BETWEEN TO_DATE (TO_CHAR (in_start_date),'dd-mon-yy')
                                        AND TO_DATE (TO_CHAR (in_end_date),'dd-mon-yy')
                 ;

      --ORDER BY p.code;
      rec              c_module%ROWTYPE;
      actualsizeucp    NUMBER;
      createdsizeucp   NUMBER;

      CURSOR c_conv0
      IS
         SELECT rec.actual_size / sloc AS ucp,
                               -- refer from Insight coding common.WorkProduct
                  (rec.actual_size / sloc)
                * (1 - NVL (rec.REUSE / 100, 0)) AS createdsizeucp
           FROM conversion
          WHERE language_id = rec.actual_size_unit_id AND method_id = 3;

      CURSOR c_conv1
      IS
         SELECT   rec.actual_size
                *              -- refer from Insight coding common.WorkProduct
                  (SELECT sloc
                     FROM conversion
                    WHERE language_id = 6 AND method_id = 3)
                / sloc AS ucp_actual,
                  (  rec.actual_size
                   * (SELECT sloc
                        FROM conversion
                       WHERE language_id = 6 AND method_id = 3)
                   / sloc
                  )
                * (1 - NVL (rec.REUSE / 100, 0)) AS ucp_created
           FROM conversion
          WHERE language_id = 6 AND method_id = rec.actual_size_unit_id;
   BEGIN
      OPEN c_module;

      LOOP
         FETCH c_module
          INTO rec;

         IF rec.REUSE IS NULL
         THEN
            rec.REUSE := 0;
         END IF;

         rec.actual_size :=
                         rec.actual_size
                         - (rec.actual_size * rec.REUSE / 100);
         actualsizeucp := 0;

         IF rec.actual_size_type = 0
         THEN
            -- Use new convertion of paper work products (Excel, Word)
            IF rec.actual_size_unit_id = 650
            THEN
               rec.actual_size_unit_id := 657;
            END IF;

            IF rec.actual_size_unit_id = 654
            THEN
               rec.actual_size_unit_id := 658;
            END IF;

            OPEN c_conv0;

            FETCH c_conv0
             INTO actualsizeucp, createdsizeucp;

            CLOSE c_conv0;
         ELSE
            OPEN c_conv1;

            FETCH c_conv1
             INTO actualsizeucp, createdsizeucp;

            CLOSE c_conv1;
         END IF;

         PIPE ROW (module_record (rec.project_id,
                                  rec.code,
                                  rec.CATEGORY,
                                  rec.wp_id,
                                  actualsizeucp,
                                  createdsizeucp,
                                  rec.group_name
                                 ));
         EXIT WHEN c_module%NOTFOUND;
      END LOOP;

      CLOSE c_module;

      RETURN;
   END;

   /* Get modules of projects that closed betwwen start_date and end_date */
    -- Date format dd-MON-yy
   FUNCTION getmodules_ongoing (
      in_start_date   IN   VARCHAR2,
      in_end_date     IN   VARCHAR2,
      cat             IN   NUMBER
   )
      RETURN module_record_set PIPELINED
   IS
      CURSOR c_module
      IS
         SELECT p.project_id, p.code, p.CATEGORY, p.group_name, m.planned_size, m.REUSE,
                planned_size_unit_id, planned_size_type, wp_id, m.note
           FROM project p, module m
          WHERE p.status IN (0, 1, 3) AND p.project_id = m.project_id
--           AND m.note like '%VC++%'
--                 AND category=cat
/*                 AND (actual_finish_date BETWEEN TO_DATE(TO_CHAR (in_start_date),
                                                       'dd-mon-yy')
                                            AND TO_DATE (TO_CHAR (in_end_date),
                                                         'dd-mon-yy')
                     OR start_date BETWEEN TO_DATE (TO_CHAR (in_start_date),
                                                       'dd-mon-yy')
                                            AND TO_DATE (TO_CHAR (in_end_date),
                                                         'dd-mon-yy')
                     )*/
      ;

      --ORDER BY p.code;
      rec       c_module%ROWTYPE;
      sizeucp   NUMBER;

      CURSOR c_conv0
      IS
         SELECT rec.planned_size / sloc AS ucp
                              -- refer from Insight coding common.WorkProduct
           FROM conversion
          WHERE language_id = rec.planned_size_unit_id AND method_id = 3;

      CURSOR c_conv1
      IS
         SELECT   rec.planned_size
                *              -- refer from Insight coding common.WorkProduct
                  (SELECT sloc
                     FROM conversion
                    WHERE language_id = 6 AND method_id = 3)
                / sloc AS ucp
           FROM conversion
          WHERE language_id = 6 AND method_id = rec.planned_size_unit_id;
   BEGIN
      OPEN c_module;

      LOOP
         FETCH c_module
          INTO rec;

         IF rec.REUSE IS NULL
         THEN
            rec.REUSE := 0;
         END IF;

         rec.planned_size :=
                       rec.planned_size
                       - (rec.planned_size * rec.REUSE / 100);
         sizeucp := 0;

         IF rec.planned_size_type = 0
         THEN
            /*
               IF rec.PLANNED_SIZE_UNIT_ID=650 THEN
                  rec.PLANNED_SIZE_UNIT_ID := 657;
               END IF;
               IF rec.PLANNED_SIZE_UNIT_ID=654 THEN
                  rec.PLANNED_SIZE_UNIT_ID := 658;
               END IF;
            */
            OPEN c_conv0;

            FETCH c_conv0
             INTO sizeucp;

            CLOSE c_conv0;
         ELSE
            OPEN c_conv1;

            FETCH c_conv1
             INTO sizeucp;

            CLOSE c_conv1;
         END IF;

         PIPE ROW (module_record (rec.project_id,
                                  rec.code,
                                  rec.CATEGORY,
                                  rec.wp_id,
                                  sizeucp,
                                  NULL,
                                  rec.group_name
                                 ));
--       PIPE ROW (module_record(rec.project_id,rec.code,null,rec.wp_id,sizeUCP,rec.note, null));
         EXIT WHEN c_module%NOTFOUND;
      END LOOP;

      CLOSE c_module;

      RETURN;
   END;

   /* Get deliverable modules of projects that running betwwen start_date and end_date */
    -- Date format dd-MON-yy
   FUNCTION getmodules_deliverable (
      in_start_date   IN   VARCHAR2,
      in_end_date     IN   VARCHAR2
   )
      RETURN deliverable_record_set PIPELINED
   IS
      CURSOR c_module
      IS
         SELECT m.module_id, m.NAME AS module_name, p.project_id, p.code,
                p.start_date,p.group_name,
                NVL (m.replanned_release_date,
                     m.planned_release_date
                    ) AS planned_release,
                m.actual_release_date
           FROM project p, module m
          WHERE p.status IN (0, 1, 3)
            AND p.project_id = m.project_id
            AND m.is_deliverable = 1
                                    /*AND (actual_finish_date BETWEEN TO_DATE(TO_CHAR (in_start_date),
                                                                          'dd-mon-yy')
                                                               AND TO_DATE (TO_CHAR (in_end_date),
                                                                            'dd-mon-yy')
                                        OR start_date BETWEEN TO_DATE (TO_CHAR (in_start_date),
                                                                          'dd-mon-yy')
                                                               AND TO_DATE (TO_CHAR (in_end_date),
                                                                            'dd-mon-yy')
                                        )*/
      ;

      --ORDER BY p.code;
      rec              c_module%ROWTYPE;
      deviation        NUMBER;
      stagebegindate   DATE;
      s1               NUMBER;
      s2               NUMBER;

      CURSOR c_milestone
      IS
-- refer from Insight coding WorkProduct.getModuleListSchedule(), Schedule.getStageList()
         SELECT   MAX (NVL (plan_finish_date, actual_finish_date))
                + 1 AS next_begin
           FROM milestone
          WHERE project_id = rec.project_id
            AND NVL (plan_finish_date, actual_finish_date) < rec.planned_release;
   BEGIN
      stagebegindate := NULL;

      OPEN c_module;

      LOOP
         FETCH c_module
          INTO rec;

         IF stagebegindate = NULL
         THEN
            stagebegindate := rec.start_date;
         END IF;

         OPEN c_milestone;

         FETCH c_milestone
          INTO stagebegindate;

         CLOSE c_milestone;

         deviation := 0;
         s1 := rec.actual_release_date - rec.planned_release;
         s2 := rec.planned_release - stagebegindate;

         IF s2 <> 0
         THEN
            deviation := s1 * 100 / s2;
         END IF;

         PIPE ROW (deliverable_record (rec.module_id,
                                       rec.module_name,
                                       rec.project_id,
                                       rec.code,
                                       rec.planned_release,
                                       rec.actual_release_date,
                                       stagebegindate,
                                       deviation,
                                       rec.group_name
                                      ));
         EXIT WHEN c_module%NOTFOUND;
      END LOOP;

      CLOSE c_module;

      RETURN;
   END;

   /* Get a metric of projects that running betwwen start_date and end_date */
    -- Date format dd-MON-yy
   FUNCTION getmetric (
      in_start_date     IN   VARCHAR2,
      in_end_date       IN   VARCHAR2,
      in_metric_index   IN   NUMBER
   )
      RETURN deliverable_record_set PIPELINED
   IS
      CURSOR c_project
      IS
         SELECT project_id, code, NAME, group_name
           FROM project
          WHERE status IN (0, 1, 3)
                                   /*AND (actual_finish_date BETWEEN TO_DATE(TO_CHAR (in_start_date),
                                                                         'dd-mon-yy')
                                                              AND TO_DATE (TO_CHAR (in_end_date),
                                                                           'dd-mon-yy')
                                       OR start_date BETWEEN TO_DATE (TO_CHAR (in_start_date),
                                                                         'dd-mon-yy')
                                                              AND TO_DATE (TO_CHAR (in_end_date),
                                                                           'dd-mon-yy')
                                       )*/
      ;

      rec         c_project%ROWTYPE;
      act_value   NUMBER;

      CURSOR c_metrics
      IS
-- refer from Insight coding WorkProduct.getModuleListSchedule(), Schedule.getStageList()
         SELECT actual_value
           FROM metrics
          WHERE project_code = rec.code AND metric_index = in_metric_index;
   BEGIN
      OPEN c_project;

      LOOP
         FETCH c_project
          INTO rec;

         EXIT WHEN c_project%NOTFOUND;
         act_value := 0;

         OPEN c_metrics;

         FETCH c_metrics
          INTO act_value;

         CLOSE c_metrics;

         PIPE ROW (deliverable_record (NULL,
                                       NULL,
                                       rec.project_id,
                                       rec.code,
                                       NULL,
                                       NULL,
                                       NULL,
                                       act_value,
                                       rec.group_name
                                      ));
      END LOOP;

      CLOSE c_project;

      RETURN;
   END;

   /* Get defects of project that closed betwwen start_date and end_date */
   -- Date format dd-MON-yy
   FUNCTION getdefectsdist (
      in_start_date   IN   VARCHAR2,
      in_end_date     IN   VARCHAR2,
      cat             IN   NUMBER
   )
      RETURN defect_record_set PIPELINED
   IS
      CURSOR c_defect
      IS
         SELECT p.project_id, p.code, p.customer, DECODE (p.category, 0, 'Development', 1, 'Maintenance', 2, 'Others') as project_category, pl.domain, DECODE (pl.application_type, 6, 'Back-end', 7, 'Business Management', 4, 'Develop GUIs', 10, 'E-Commercial & M-business', 15, 'Embedded Software', 9, 'Intelligent data & Information Retrieval', 14, 'Mainframe application', 8, 'Management information system', 11, 'Middle ware', 1, 'Normal application', 12, 'Other', 5, 'Unit Test', 2, 'Web application', 3, 'Web application & Web engineering') as application_type,
                d.qa_id, d.process_id, group_name ,
                d.defs_id AS severity_id, d.wp_id
           FROM defect d, project p, project_plan pl
          WHERE ds_id <> 6
            --AND qa_id <> 15                              -- After release test
            --AND qa_id <> 22                            -- After release review
               -- Get Leakage:
            --and qa_id in (15,22,13)
            AND p.project_id = d.project_id
            AND p.project_id = pl.project_id
            AND p.status = 1                                     -- in (0,1,3)
            AND actual_finish_date
                      BETWEEN TO_DATE (TO_CHAR (in_start_date),'dd-mon-yy')
                                        AND TO_DATE (TO_CHAR (in_end_date),'dd-mon-yy')
            --AND p.group_name in ('G3', 'G6', 'G9', 'G0-HCM', 'G12', 'SU15', 'SU18', 'G33', 'SU39')
                 ;

      rec_defect   c_defect%ROWTYPE;
      weighted     NUMBER;
   BEGIN
      OPEN c_defect;

      LOOP
         FETCH c_defect
          INTO rec_defect;

         IF rec_defect.severity_id = 1
         THEN                                                        -- Fatal
            weighted := 10;
         ELSIF rec_defect.severity_id = 2
         THEN
            weighted := 5;
         ELSIF rec_defect.severity_id = 3
         THEN
            weighted := 3;
         ELSE
            weighted := 1;
         END IF;

         PIPE ROW (defect_record (rec_defect.project_id,
                                  rec_defect.code,
                                  NULL,
                                  rec_defect.qa_id,
                                  rec_defect.process_id,
                                  rec_defect.wp_id,
                                  weighted,
                                  0,
                                  rec_defect.group_name,
                                  rec_defect.group_name
                                 ));
         EXIT WHEN c_defect%NOTFOUND;
      END LOOP;

      CLOSE c_defect;

      RETURN;
   END;

END pkg_project_stage;
/
CREATE FUNCTION F_DETECTEDBY(intValue IN NUMBER, strID IN VARCHAR)
RETURN NUMBER
IS
nCount NUMBER;
BEGIN
SELECT COUNT(NC.NCID) INTO nCount FROM NC WHERE DetectedBy=intValue AND NC.CreationDate<=SYSDATE AND NC.CreationDate<=SYSDATE GROUP BY NC.ProjectID HAVING NC.ProjectID=strID;
RETURN nCount;
END;
/
CREATE FUNCTION F_ISOCLAUSE(intValue IN NUMBER, strID IN VARCHAR)
RETURN NUMBER
IS
nCount NUMBER;
BEGIN
SELECT COUNT(NC.NCID) INTO nCount FROM NC WHERE ISOClause=intValue AND NC.CreationDate<=SYSDATE AND NC.CreationDate<=SYSDATE GROUP BY NC.ProjectID HAVING NC.ProjectID=strID;
RETURN nCount;
END;
/
CREATE FUNCTION F_KPA(intValue IN NUMBER, strID IN VARCHAR)
RETURN NUMBER
IS
nCount NUMBER;
BEGIN
SELECT COUNT(NC.NCID) INTO nCount FROM NC WHERE KPA=intValue AND NC.CreationDate<=SYSDATE AND NC.CreationDate<=SYSDATE GROUP BY NC.GroupName HAVING NC.GroupName=strID;
RETURN nCount;
END;
/
CREATE FUNCTION F_NCLEVEL(intValue IN NUMBER, strID IN VARCHAR)
RETURN NUMBER
IS
nCount NUMBER;
BEGIN
SELECT COUNT(NC.NCID) INTO nCount FROM NC WHERE NCLevel=intValue AND NC.CreationDate<=SYSDATE AND NC.CreationDate<=SYSDATE GROUP BY NC.GroupName HAVING NC.GroupName=strID;
RETURN nCount;
END;
/
CREATE FUNCTION F_NCTYPE(intValue IN NUMBER, strID IN VARCHAR)
RETURN NUMBER
IS
nCount NUMBER;
BEGIN
SELECT COUNT(NC.NCID) INTO nCount FROM NC WHERE NCType=intValue AND NC.CreationDate<=SYSDATE AND NC.CreationDate<=SYSDATE GROUP BY NC.ProjectID HAVING NC.ProjectID=strID;
RETURN nCount;
END;
/
CREATE FUNCTION F_PROCESS(intValue IN NUMBER, strID IN VARCHAR)
RETURN NUMBER
IS
nCount NUMBER;
BEGIN
SELECT COUNT(NC.NCID) INTO nCount FROM NC WHERE Process=intValue AND NC.CreationDate<=SYSDATE AND NC.CreationDate<=SYSDATE GROUP BY NC.ProjectID HAVING NC.ProjectID=strID;
RETURN nCount;
END;
/
CREATE FUNCTION F_TYPEOFACTION(intValue IN NUMBER, strID IN VARCHAR)
RETURN NUMBER
IS
nCount NUMBER;
BEGIN
SELECT COUNT(NC.NCID) INTO nCount FROM NC WHERE TypeOfAction=intValue AND NC.CreationDate<=SYSDATE AND NC.CreationDate<=SYSDATE GROUP BY NC.ProjectID HAVING NC.ProjectID=strID;
RETURN nCount;
END;
/
CREATE FUNCTION F_TYPEOFCAUSE(intValue IN NUMBER, strID IN VARCHAR)
RETURN NUMBER
IS
nCount NUMBER;
BEGIN
SELECT COUNT(NC.NCID) INTO nCount FROM NC WHERE TypeOfCause=intValue AND NC.CreationDate<=SYSDATE AND NC.CreationDate<=SYSDATE GROUP BY NC.ProjectID HAVING NC.ProjectID=strID;
RETURN nCount;
END;
/
commit






CREATE INDEX ARCHIVE_EFF_BYTYPE_PRJ_IDX
	ON ARCHIVE_ACTUAL_EFFORT_BY_TYPE(PROJECT_ID)
/
CREATE INDEX IDX_ASSIGNMENT_PRJ_ID
	ON ASSIGNMENT(PROJECT_ID)
/
CREATE UNIQUE INDEX I_AUDIT_ACTIONS
	ON AUDIT_ACTIONS(ACTION, NAME)
/
CREATE INDEX IDX_DEFECT_ATT_DEFID
	ON DEFECT_ATTACHMENT(DEFECT_ID)
/
CREATE INDEX DP_I1
	ON DEFECT_PERMISSION(DEVELOPER_ID)
/
CREATE UNIQUE INDEX SYS_C001234
	ON DEFECT_PLAN(DP_ID)
/
CREATE INDEX DQ_DEV_I2
	ON DEFECT_QUERY(DEVELOPER_ID)
/
CREATE INDEX DQ_PRO_I2
	ON DEFECT_QUERY(PROJECT_ID)
/
CREATE INDEX DEFECT_I2
	ON DEFECT(PROJECT_ID, DS_ID)
/
CREATE INDEX DEFECT_I4
	ON DEFECT(CREATE_DATE)
/
CREATE INDEX DEFECT_I3
	ON DEFECT(ASSIGNED_TO)
/
CREATE INDEX MILESTONE_I3
	ON MILESTONE(BASE_FINISH_DATE)
/
CREATE INDEX MILESTONE_I2
	ON MILESTONE(PLAN_FINISH_DATE)
/
CREATE INDEX IND_PROJ_ID
	ON MILESTONE(PROJECT_ID)
/
CREATE INDEX MOD_PRO_I1
	ON MODULE(PROJECT_ID)
/
CREATE INDEX IDX_NCPERMISSION_DEVELOPER_ID
	ON NCPERMISSION(DEVELOPER_ID)
/
CREATE INDEX OPENISSUE_I1
	ON OPENISSUE(PROJECT_ID)
/
CREATE INDEX IND_PLANENDDATE
	ON OTHER_ACTIVITY(PLANNED_END_DATE)
/
CREATE INDEX XIF6OTHER_ASSIGNMENT
	ON OTHER_ASSIGNMENT(DEVELOPER_ID)
/
CREATE INDEX OA_I2
	ON OTHER_ASSIGNMENT(FROM_DATE)
/
CREATE INDEX IDX_PRODUCT_LOC_ACTUAL_PROJECT
	ON PRODUCT_LOC_ACTUAL(PROJECT_ID)
/
CREATE INDEX IDX_PRODUCT_LOC_PLAN_PROJECT
	ON PRODUCT_LOC_PLAN(PROJECT_ID)
/
CREATE INDEX PROJECT_I1
	ON PROJECT(LEADER)
/
CREATE INDEX PROJECT_I2
	ON PROJECT(GROUP_NAME)
/
CREATE INDEX IDX_PROJECTSKILL_ASS_ID
	ON PROJECTSKILL(ASSIGNMENT_ID)
/
CREATE UNIQUE INDEX I_STMT_AUDIT_OPTION_MAP
	ON STMT_AUDIT_OPTION_MAP(OPTION#, NAME)
/
CREATE UNIQUE INDEX I_SYSTEM_PRIVILEGE_MAP
	ON SYSTEM_PRIVILEGE_MAP(PRIVILEGE, NAME)
/
CREATE UNIQUE INDEX I_TABLE_PRIVILEGE_MAP
	ON TABLE_PRIVILEGE_MAP(PRIVILEGE, NAME)
/
CREATE INDEX IDX_ATT_DEVELOPER_ID
	ON TIMESHEET_EXEMPTION(DEVELOPER_ID)
/
CREATE INDEX XIF7TIMESHEET
	ON TIMESHEET(PROJECT_ID)
/
CREATE INDEX TIMESHEET_I4
	ON TIMESHEET(OCCUR_DATE)
/
CREATE INDEX TIMESHEET_I3
	ON TIMESHEET(APPROVED_BY_LEADER)
/
CREATE INDEX XIF8TIMESHEET
	ON TIMESHEET(DEVELOPER_ID)
/
CREATE INDEX IDX_WORKUNIT_TABLEID
	ON WORKUNIT(TABLEID)
/
CREATE TRIGGER DEFECT_PLAN_BI
 BEFORE 
 INSERT
 ON DEFECT_PLAN
 REFERENCING OLD AS OLD NEW AS NEW
 FOR EACH ROW 





DECLARE
    V_ID NUMBER;
BEGIN
    SELECT DEFECT_PLAN_SEQ.NEXTVAL INTO V_ID FROM DUAL;
    :NEW.DP_ID:=V_ID;
END;

/
CREATE TRIGGER QUERY_BI
  BEFORE INSERT
  ON DEFECT_QUERY
  FOR EACH ROW








DECLARE
    V_ID NUMBER;
BEGIN
    SELECT QUERY_SEQ.NEXTVAL INTO V_ID FROM DUAL;
    :NEW.QUERY_ID:=V_ID;
END;

/
CREATE TRIGGER DEFECT_BU
 BEFORE 
 UPDATE
 ON DEFECT
 REFERENCING OLD AS OLD NEW AS NEW
 FOR EACH ROW 



DECLARE
    V_NEWSTATUS VARCHAR2(30);
    V_OLDSTATUS VARCHAR2(30);
    CURSOR C1 IS
        SELECT 1 FROM DEFECT_HISTORY WHERE DEFECT_ID=:OLD.DEFECT_ID;
    V_DUMMY NUMBER;
    V_APPENDLENGTH NUMBER := 0;
    MAX_LENGTH CONSTANT NUMBER := 1024;
    V_HISTORY VARCHAR(1024);
BEGIN
    IF :NEW.DS_ID IS NULL OR :OLD.DS_ID IS NULL THEN
        RETURN;
    END IF;
    IF (:NEW.DS_ID=:OLD.DS_ID) THEN
        RETURN;
    END IF;

-- UPDATE CLOSE DATE
    IF (:NEW.DS_ID=4 OR :NEW.DS_ID=5) THEN --TESTED OR ACCEPTED
        :NEW.CLOSE_DATE:=TRUNC(SYSDATE);
    END IF;
----------------------------------------------------------------------------
-- UPDATE HISTORY
----------------------------------------------------------------------------
    SELECT NAME INTO V_NEWSTATUS FROM DEFECT_STATUS WHERE DS_ID=:NEW.DS_ID;
    SELECT NAME INTO V_OLDSTATUS FROM DEFECT_STATUS WHERE DS_ID=:OLD.DS_ID;

    -- GET LENGTH OF NEW HISTORY LINE
    SELECT LENGTH('- '|| TO_CHAR(SYSDATE,'DD-MON-YY HH24:MM')|| ': '|| :NEW.UPDATED_BY || ' CHANGED STATUS FROM ' ||V_OLDSTATUS || ' TO ' ||V_NEWSTATUS || CHR(10)||CHR(13))
        INTO V_APPENDLENGTH FROM DUAL; 

    OPEN C1;
    FETCH C1 INTO V_DUMMY;
    IF C1%NOTFOUND THEN
-- HISTORY NOT FOUND
        INSERT INTO DEFECT_HISTORY (DEFECT_ID, HISTORY) VALUES (:NEW.DEFECT_ID,'- '|| TO_CHAR(SYSDATE,'DD-MON-YY')|| ': '||:NEW.UPDATED_BY || ' CHANGED STATUS FROM ' ||V_OLDSTATUS || ' TO ' ||V_NEWSTATUS || CHR(10)||CHR(13));
    ELSE
--HISTORY FOUND
        -- CUT THE HISTORY STRING IF LENGTH IS OVER MAXIMUM LENGTH
        SELECT HISTORY INTO V_HISTORY FROM DEFECT_HISTORY WHERE DEFECT_ID=:OLD.DEFECT_ID;
        WHILE ((LENGTH(V_HISTORY) + V_APPENDLENGTH) > MAX_LENGTH) LOOP
            V_HISTORY := SUBSTR(V_HISTORY, INSTR(V_HISTORY, CHR(13), 1, 1)+1);
        END LOOP;

        UPDATE DEFECT_HISTORY SET HISTORY=V_HISTORY ||'- '|| TO_CHAR(SYSDATE,'DD-MON-YY HH24:MM')|| ': '|| :NEW.UPDATED_BY || ' CHANGED STATUS FROM ' ||V_OLDSTATUS || ' TO ' ||V_NEWSTATUS || CHR(10)||CHR(13)
            WHERE DEFECT_ID=:OLD.DEFECT_ID;
    END IF;

    CLOSE C1;
END;

/
CREATE TRIGGER BEFORE_INSERT_DEVELOPER
 BEFORE 
 INSERT
 ON DEVELOPER
 REFERENCING OLD AS OLD NEW AS NEW
 FOR EACH ROW 





BEGIN
   SELECT DEVELOPER_SEQ.NEXTVAL INTO :NEW.DEVELOPER_ID  FROM DUAL;
END;

/
CREATE TRIGGER DEVELOPER_AI AFTER INSERT ON DEVELOPER
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW





BEGIN
    -- INSERT PERMISSION FOR NCMS
    INSERT INTO NCPERMISSION(DEVELOPER_ID, TOOL, ROLE)
    VALUES (:NEW.DEVELOPER_ID, 0, :NEW.ROLE);
    -- INSERT PERMISSION FOR CALLLOG
    INSERT INTO NCPERMISSION(DEVELOPER_ID, TOOL, ROLE)
    VALUES (:NEW.DEVELOPER_ID, 1, :NEW.ROLE);
END;

/
CREATE TRIGGER DEVELOPER_AU
AFTER UPDATE
ON DEVELOPER
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW





BEGIN
    -- UPDATE PERMISSION FOR NCMS (TOOL=0)
    UPDATE NCPERMISSION
    SET ROLE=:NEW.ROLE
    WHERE DEVELOPER_ID=:NEW.DEVELOPER_ID AND TOOL=0;
END;

/
CREATE TRIGGER DEVELOPER_BD
 BEFORE 
 DELETE
 ON DEVELOPER
 REFERENCING OLD AS OLD NEW AS NEW
 FOR EACH ROW 




BEGIN
    -- REMOVE PERMISSION FROM NCMS AND CALLLOG
    DELETE NCPERMISSION
    WHERE DEVELOPER_ID = :OLD.DEVELOPER_ID;
END;

/
CREATE TRIGGER MODULE_BD
 BEFORE 
 DELETE
 ON MODULE
 REFERENCING OLD AS OLD NEW AS NEW
 FOR EACH ROW 




BEGIN
   UPDATE DEFECT
      SET MODULE_ID = 0
    WHERE MODULE_ID = :OLD.MODULE_ID;
END;

/
CREATE TRIGGER MODULE_BI
BEFORE  INSERT
ON MODULE
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW








DECLARE
    V_ID NUMBER;
BEGIN
    SELECT MODULE_SEQ.NEXTVAL INTO V_ID FROM DUAL;
    :NEW.MODULE_ID:=V_ID;
END;

/
CREATE TRIGGER BEFORE_INSERT_NC
 BEFORE 
 INSERT
 ON NC
 REFERENCING OLD AS OLD NEW AS NEW
 FOR EACH ROW 





BEGIN
   SELECT NC_SEQ.NEXTVAL INTO :NEW.NCID  FROM DUAL;
END;

/
CREATE TRIGGER NC_BU
BEFORE UPDATE 
ON NC
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW




DECLARE
    V_NEWSTATUS VARCHAR2(30);
    V_OLDSTATUS VARCHAR2(30);
    CURSOR C1 IS
        SELECT 1 FROM NCHISTORY WHERE NCID=:OLD.NCID;
    V_DUMMY NUMBER;
    V_APPENDLENGTH NUMBER := 0;
    MAX_LENGTH CONSTANT NUMBER := 1024;
    V_HISTORY VARCHAR(1024);
BEGIN
    IF (:NEW.STATUS=:OLD.STATUS) THEN
        RETURN;
    END IF;
----------------------------------------------------------------------------
-- UPDATE HISTORY
----------------------------------------------------------------------------
    -- GET LENGTH OF NEW HISTORY LINE
    SELECT LENGTH('- '|| TO_CHAR(SYSDATE,'DD-MON-YY HH24:MI')|| ': '|| :NEW.UPDATED_BY || ' CHANGED STATUS FROM ' ||V_OLDSTATUS || ' TO ' ||V_NEWSTATUS || CHR(10)||CHR(13))
        INTO V_APPENDLENGTH FROM DUAL; 

    SELECT DESCRIPTION INTO V_NEWSTATUS FROM NCCONSTANT WHERE CONSTANTID=:NEW.STATUS;
    SELECT DESCRIPTION INTO V_OLDSTATUS FROM NCCONSTANT WHERE CONSTANTID=:OLD.STATUS;

    OPEN C1;
    FETCH C1 INTO V_DUMMY;
    IF C1%NOTFOUND THEN
-- HISTORY NOT FOUND
        INSERT INTO NCHISTORY (NCID, HISTORY) VALUES (:NEW.NCID,'- '|| TO_CHAR(SYSDATE,'DD-MON-YY HH24:MI')|| ': '||:NEW.UPDATED_BY || ' CHANGED STATUS FROM ' ||V_OLDSTATUS || ' TO ' ||V_NEWSTATUS || CHR(10)||CHR(13));
    ELSE
--HISTORY FOUND
        -- CUT OFF THE BEGINING OF HISTORY STRING IF LENGTH IS OVER MAXIMUM LENGTH
        SELECT HISTORY INTO V_HISTORY FROM NCHISTORY WHERE NCID=:OLD.NCID;
        WHILE ((LENGTH(V_HISTORY) + V_APPENDLENGTH) > MAX_LENGTH) LOOP
            V_HISTORY := SUBSTR(V_HISTORY, INSTR(V_HISTORY, CHR(13), 1, 1)+1);
        END LOOP;
        
        UPDATE NCHISTORY SET HISTORY=HISTORY ||'- '|| TO_CHAR(SYSDATE,'DD-MON-YY HH24:MI')|| ': '|| :NEW.UPDATED_BY || ' CHANGED STATUS FROM ' ||V_OLDSTATUS || ' TO ' ||V_NEWSTATUS || CHR(10)||CHR(13)
            WHERE NCID=:OLD.NCID;
    END IF;

    CLOSE C1;
END;

/
CREATE TRIGGER BEFORE_INSERT_CONSTANT
BEFORE INSERT ON NCCONSTANT
FOR EACH ROW








BEGIN
   SELECT NCCONSTANT_SEQ.NEXTVAL INTO :NEW.CONSTANTID  FROM DUAL;
END;

/
CREATE TRIGGER BEFORE_INSERT_VIEW
BEFORE INSERT 
ON NCVIEW
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW





BEGIN
   SELECT NCVIEW_SEQ.NEXTVAL INTO :NEW.VIEWID  FROM DUAL;
END;

/
CREATE TRIGGER OPENISSUE_BI
  BEFORE INSERT
  ON OPENISSUE

  FOR EACH ROW








DECLARE
    V_ID NUMBER;
BEGIN
    SELECT ISSUE_SEQ.NEXTVAL INTO V_ID FROM DUAL;
    :NEW.OPENISSUE_ID:=V_ID;
END;

/
CREATE TRIGGER OA_BI
 BEFORE 
 INSERT
 ON OTHER_ASSIGNMENT
 REFERENCING OLD AS OLD NEW AS NEW
 FOR EACH ROW 




DECLARE
    V_ID NUMBER;
BEGIN
    SELECT OA_SEQ.NEXTVAL INTO V_ID FROM DUAL;
    :NEW.OA_ID:=V_ID;
END;

/
CREATE TRIGGER PROC_WP_BI
BEFORE INSERT 
ON PROCESS_WP_MAPPING
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW





DECLARE
   V_ID   NUMBER;
BEGIN
   SELECT PROC_WP_SEQ.NEXTVAL
     INTO V_ID
     FROM DUAL;

   :NEW.PROC_WP_ID := V_ID;
END;

/
CREATE TRIGGER SET_CODE_STAFFS
 BEFORE 
 INSERT
 ON STAFFS
 REFERENCING OLD AS OLD NEW AS NEW
 FOR EACH ROW 




DECLARE
CODE_NO NUMBER;
BEGIN
  SELECT STAFFS_SEQ.NEXTVAL INTO CODE_NO
  FROM DUAL;
  :NEW.CODE :=CODE_NO;
END
;

/
CREATE TRIGGER TIMESHEET_EXEMPTION_BI
 BEFORE
  INSERT
 ON TIMESHEET_EXEMPTION
REFERENCING NEW AS NEW OLD AS OLD
 FOR EACH ROW



DECLARE
    V_ID NUMBER;
BEGIN
    SELECT TIMESHEET_EXEMPTION_SEQ.NEXTVAL INTO V_ID FROM DUAL;
    :NEW.EXEMPTION_ID:=V_ID;
END;

/
CREATE TRIGGER TIMESHEET_ARCHIVE_BI
 BEFORE
  INSERT
 ON TIMESHEET_OLD_DATA
REFERENCING NEW AS NEW OLD AS OLD
 FOR EACH ROW



DECLARE
    V_ID NUMBER;
BEGIN
    SELECT TIMESHEET_ARCHIVE_SEQ.NEXTVAL INTO V_ID FROM DUAL;
    :NEW.TIMESHEET_ID:=V_ID;
END;

/
CREATE TRIGGER TIMESHEET_BI
  BEFORE INSERT
  ON TIMESHEET

  FOR EACH ROW









DECLARE
    V_ID NUMBER;
BEGIN
    SELECT TIMESHEET_SEQ.NEXTVAL INTO V_ID FROM DUAL;
    :NEW.TIMESHEET_ID:=V_ID;
END;

/
quit
