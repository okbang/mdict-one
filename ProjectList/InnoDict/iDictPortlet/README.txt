Innova Dictionary Portlet

How to run within Eclipse + Glashfish + Open Portlet Container
==================================================================
Modify file "resource/app.properties", property "DictRepo" specifies the path of folder contains Stardict dictionaries


How to prepare the dictionary repository for unit testing
==================================================================
1) Download Stardict dictionaries for testing at:
- http://open-ones.googlecode.com/files/stardict-dictd_viet-anh-2.4.2.tar.gz
- http://open-ones.googlecode.com/files/stardict-dictd_anh-viet-2.4.2.tar.gz

2) Create folder "dict-repo" below folder "/iDictPortlet/src_ut" (iDictPortlet is the project folder)

3) Uncompress .tar.gz files into the folder "/iDictPortlet/src_ut/dict-repo". It is called "Dictionary Repository" (DictRepo)

Now, you can run Test Cases in /iDictPortlet/src_ut/openones/idict/biz/DictBizTest.java

Discussion Board:
==================================================================
http://www.open-ones.com/ishare/posts/list/35.page

Version up
==================================================================
0.0.4 (Status: Development)
- Remove the framework of MVCPortlet (http://mvcportlet.googlecode.com/)
- Support Ajax in Lookup screen

0.0.3:
- Using Spring MVC Portlet Framework 3.1