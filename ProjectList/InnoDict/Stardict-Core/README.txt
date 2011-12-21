This is core utility of stardict.
How to distribute it
==========================================
    run dist.bat
    (get the binary package in folder dist)
    
+How to look up a word
==========================================
    Use the following syntax
    
    Dictionary dict = new Dictionary(url);
    
    url is path to one of three files in dict folder or path to its folder.
    - example1: dict folder name is "stardict-english-2.4.2"
    and it in "D:\dict\" file name is "longman" then url is
    "D:\\dict\\stardict-english-2.4.2\\longman.ifo" or 
    "D:\\dict\\stardict-english-2.4.2\\longman.idx" or 
    "D:\\dict\\stardict-english-2.4.2\\longman.dict"
    
    - example2: dict folder name is "stardict-english-2.4.2"
    and it in "D:\dict\" then url is "D:\\dict\\stardict-english-2.4.2"
    
    To look up a word, use the following syntax
    dict.lookupWord("word");
    word is what you want to look up.
    
    ******************************************
    FOR MORE DETAILS ABOUT CLASSES, READ IN JAVADOC FOLDER

Long's comment
    I've modified something, then, You will get more comfort to run the test
    I leave 2 dictionaries in folder dictionariesToTest in this project folder
    When checkout this project into you local drive, 
    just edit the constant DICT_FILE_PATH in StarDictTest.java to              
    your .dict file path instead of my path and run the test.
    I used the stardict-dictd-easton-2.4.2, but you're free to use any
    StarDict dictionary, just remember:
    This version can read a "dictionary" with only 3 files (.ifo, .idx, .dict) 
    If you get a dictionary with more than 3 files, please chose another dictionary
    to test. If you get a dictionary with 3 files, without the .dict but the
    .dict.dz or .dict.gz, just use 7zip to extract it to an .dict file.
    (if you download the stardict-dictd-easton-2.4.2 from 
    http://yeelou.com/huzheng/stardict-dic/misc/
    you'll get the dictd_www.dict.org_easton.dict.dz, not the dictd_www.dict.org_easton.dict
    I did the extracting to test this project. 