/****** Object:  ForeignKey [FK_SVNFILE_DIR]    Script Date: 11/29/2010 14:50:48 ******/
ALTER TABLE [SVNFile] DROP CONSTRAINT [FK_SVNFILE_DIR] 
GO

/****** Object:  ForeignKey [FK_SVNFILE_DIR]    Script Date: 11/29/2010 14:50:48 ******/
ALTER TABLE [SVNVersion] DROP CONSTRAINT [FK_SVNVERSION_DIR] 
GO

truncate table [dbo].[Dir]
truncate table [dbo].[SVNVersion]
truncate table [dbo].[SVNFile]
GO
/****** Object:  ForeignKey [FK_SVNFILE_DIR]    Script Date: 11/29/2010 14:50:48 ******/
ALTER TABLE [SVNFile] ADD CONSTRAINT [FK_SVNFILE_DIR] FOREIGN KEY([DirID]) REFERENCES [Dir] ([DirID])
GO

/****** Object:  ForeignKey [FK_SVNFILE_DIR]    Script Date: 11/29/2010 14:50:48 ******/
ALTER TABLE [SVNVersion] ADD CONSTRAINT [FK_SVNVersion_DIR] FOREIGN KEY([DirID])
REFERENCES [Dir] ([DirID])
GO
ALTER TABLE [SVNVersion] CHECK CONSTRAINT [FK_SVNVersion_DIR]
GO


-- truncate for table  dbo.SVNRepo
ALTER TABLE [Dir] DROP CONSTRAINT [FK_DIR_SVNREPO] 

GO
TRUNCATE TABLE dbo.SVNRepo
GO

ALTER TABLE [Dir]   ADD  CONSTRAINT [FK_DIR_SVNREPO] FOREIGN KEY([SVNID])
REFERENCES [SVNRepo] ([SVNID])

