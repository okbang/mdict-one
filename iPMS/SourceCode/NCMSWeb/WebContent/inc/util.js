// util.js

//add by MinhPT
//16-Dec-03
//for gen Project Combo depend on Group Combo
function selectChangeGroup(currentProject, isNC) {
    var myElement;
    var form = document.forms[0];
    for (var i = form.cboProject.options.length; i >= 0; i--) {
        form.cboProject.options[i] = null;
    }
    var currentGroup = form.cboGroup.value;
    //if select All Project
    if (currentGroup == arrItemGroup[0]){

        myElement = document.createElement("option");
        myElement.value = arrProjectCode[0];
        myElement.text = arrProjectCode[0];
        form.cboProject.add(myElement);
        if (currentProject.toString() == arrProjectCode[0]){
            myElement.selected = true;
        }
        myElement = document.createElement("option");
        //NCMS system
        //if (isNC) {
            myElement.value = "General";
            myElement.text = "General";
            form.cboProject.add(myElement);
            if (currentProject.toString() == "General"){
                myElement.selected = true;
            }
        //}
        
        if (arrProjectCode.length >= 1)
            for(var i = 1;i < arrProjectCode.length; i++){
                myElement = document.createElement("option");
                myElement.value = arrProjectCode[i];
                myElement.text = arrProjectCode[i];
                form.cboProject.add(myElement);
                if (currentProject.toString() == arrProjectCode[i]){
                    myElement.selected = true;
                }
            }
    }
    else{
        myElement = document.createElement("option");
        myElement.value = "(All)";
        myElement.text = "(All)";
        form.cboProject.add(myElement);
        if (currentProject.toString() == "(All)"){
            myElement.selected = true;
        }
        myElement = document.createElement("option");
        //if (isNC) {
            myElement.value = "General";
            myElement.text = "General";
            form.cboProject.add(myElement);
            if (currentProject.toString() == "General"){
                myElement.selected = true;
            }
        //}
        
        for(var i = 0;i < arrProjectCode.length; i++){
            if(currentGroup == arrProjectGroupName[i]){
                myElement = document.createElement("option");
                myElement.value = arrProjectCode[i];
                myElement.text = arrProjectCode[i];
                form.cboProject.add(myElement);
                if (currentProject.toString() == arrProjectCode[i]){
                    myElement.selected = true;
                }
            }
        }
    }
}


