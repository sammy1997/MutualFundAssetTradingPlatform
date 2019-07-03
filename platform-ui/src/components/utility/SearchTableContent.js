
function searchContent(inputId, tableId, searchableFields) {
    var filters=[]
    var input, filter, table, tr, td, i, txtValue;
    for(var index = 0;index < searchableFields.length;index++){
        input = document.getElementById(inputId+index);
        if(input==null) continue;
        filter = input.value.toUpperCase();
        filters.push(filter);
    }
    // console.log(tableId);
    table = document.getElementById(tableId);
    tr = table.getElementsByTagName("tr");
    console.log(searchableFields)
    for (i = 2; i < tr.length; i++){
        var f = true;
        for(var index = 0;index<filters.length;index++){
            td=tr[i].getElementsByTagName("td")[searchableFields[index]];
            if(td){
                txtValue = td.textContent || td.innerText;
                if(txtValue.toUpperCase().indexOf(filters[index]) == -1){
                    f=false;
                    break;
                }
            }
        }
        if(f)
            tr[i].style.display = "";
        else
            tr[i].style.display = "none";
    }
}

export default searchContent;