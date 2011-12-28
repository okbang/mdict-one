-- Count total size (LOC) of current date
SELECT SUM(SVNVersion.NMLOC) FROM SVNVersion, Revision
WHERE SVNVersion.RevisionID = 
      (
      SELECT MAX(RevisionNum)  FROM REVISION
        WHERE CONVERT(VARCHAR(10), GetDate(),102) =  CONVERT(VARCHAR(10), Revision.DateLog,102) 
      )
      
      
-- Script 2
SELECT DIR.ParentPath, SVNVersion.FileName, SVNVersion.NMLOC, SVNVersion.NMComment  FROM SVNVersion, Revision, DIR
WHERE (SVNVersion.RevisionID = 
                              (SELECT MAX(RevisionNum)  FROM REVISION
                              WHERE CONVERT(VARCHAR(10), GetDate(),102) =  CONVERT(VARCHAR(10), Revision.DateLog,102) 
                              )
      )
      AND (SVNVersion.RevisionID = Revision.RevisionID)
      AND (SVNVersion.DirID = DIR.DirID)
      