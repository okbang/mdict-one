 SELECT Revision.DateLog, DIR.ParentPath + '/' + DIR.DirName as ParentPath, SVNVersion.FileName, Revision.Author, SVNVersion.NMLOC, SVNVersion.NMComment    
 FROM SVNVersion, Revision, DIR, SVNRepo  
 WHERE (SVNVersion.SVNAction <> 'D') 
       AND (SVNVersion.RevisionID = Revision.RevisionID) 
       AND (SVNVersion.DirID = DIR.DirID) 
       AND SVNRepo.URL = 'https://rai-server/svn/FUOJT_PreProject/trunk/DocSearch' 
       AND SVNRepo.SVNID = DIR.SVNID 
       AND SVNVersion.RevisionID = (SELECT MAX(ve2.RevisionID) 
                                    FROM SVNVersion ve2  
                                    WHERE SVNVersion.DirID = ve2.DirID 
                                    AND SVNVersion.FileName = ve2.FileName) 
 ORDER BY DIR.ParentPath, SVNVersion.FileName 
