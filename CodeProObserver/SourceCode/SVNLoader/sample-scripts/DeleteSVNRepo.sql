SELECT * FROM DIR
WHERE DIR.SVNID = 3


SELECT * FROM SVNFile 
WHERE SVNFile.DirID in 
                       (SELECT DIR.DirID FROM DIR
                       WHERE DIR.SVNID = 3)
-- 1
DELETE FROM SVNFile 
WHERE SVNFile.DirID in 
                       (SELECT DIR.DirID FROM DIR
                       WHERE DIR.SVNID = 3)


SELECT RevisionID FROM Revision
WHERE Revision.SVNID = 3                      


SELECT * FROM SVNVersion 
WHERE (SVNVersion.DirID in 
                       (SELECT DIR.DirID FROM DIR
                       WHERE DIR.SVNID = 3)
      )
      and
      (SVNVersion.RevisionID in    
                       (SELECT RevisionID FROM Revision
                       WHERE Revision.SVNID = 3           
                       )
      )
-- 2
DELETE FROM SVNVersion 
WHERE (SVNVersion.DirID in 
                       (SELECT DIR.DirID FROM DIR
                       WHERE DIR.SVNID = 3)
      )
      and
      (SVNVersion.RevisionID in    
                       (SELECT RevisionID FROM Revision
                       WHERE Revision.SVNID = 3           
                       )
      )
-- 3
DELETE FROM DIR
WHERE DIR.SVNID = 3
-- 4
DELETE FROM Revision
WHERE Revision.SVNID = 3

-- 5
DELETE FROM SVNRepo
WHERE SVNID = 3

