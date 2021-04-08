function sortTable() {
    var table, rows, switching, i, x, y, shouldSwitch;
    table = document.getElementById("table");
    switching = true;
    /* Make a loop that will continue until
    no switching has been done: */
    while (switching) {
        // Start by saying: no switching is done:
        switching = false;
        rows = table.rows;
        /* Loop through all table rows (except the
        first, which contains table headers): */
        for (i = 1; i < (rows.length - 1); i++) {
            // Start by saying there should be no switching:
            shouldSwitch = false;
            /* Get the two elements you want to compare,
            one from current row and one from the next: */
            x = rows[i].getElementsByTagName("td")[0];
            y = rows[i + 1].getElementsByTagName("td")[0];
            // Check if the two rows should switch place:
            if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                // If so, mark as a switch and break the loop:
                shouldSwitch = true;
                break;
            }
        }
        if (shouldSwitch) {
            /* If a switch has been marked, make the switch
            and mark that a switch has been done: */
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
        }
    }
}

const jsonFromHtml = (tbody, thead) => {
    let tableJson = {rows:[]};
    [...tbody.children].forEach(tableRow => {
        let rowEntry = {};
        [...tableRow.children].forEach((cell, column) => rowEntry[thead.children[0].children[column].textContent] = cell.textContent);
        tableJson.rows.push(rowEntry);
    });
    console.log(tableJson);
    return tableJson;
};

const arrayToTable = (arr, tbody, thead) => {
    let rows = [];
    arr.forEach((row, rowNum) => {
        row = [...thead.children[0].children].map(th => row[th.textContent] ? row[th.textContent] : '');
        row = row.map(td => `<td>${td}</td>`);
        row = `<tr>${row.join('')}</tr>`;
        rows.push(row);
    });
    console.log("--->"+rows)
    tbody.innerHTML = rows.join('');
};

const myTableTbody = document.querySelector('#myTable tbody');
const myTableThead = document.querySelector('#myTable thead');

const customSort = (arr, key, order) => {
    let sortCompare = order == 'Desc' ? -1 : 1;
    return arr.sort((first, second) => first[key]>second[key] ?  sortCompare : first[key]<second[key] ?  -sortCompare : 0);
};


document.querySelector('#selectionOrder').addEventListener('change', function(){
    let sortKey = this.value.match(/[A-Za-z]+/)[0];

    if(sortKey.includes("Asc")){
        sortKey = sortKey.slice(0, -3);
    }
    if(sortKey.includes("Desc")){
        sortKey = sortKey.slice(0, -4);
    }
    if(sortKey == "SharePrice"){
        sortKey = "Share Price";
    }
    console.log(sortKey);
    let sortOrder = this.value.match(/(A|De)sc/) ? this.value.match(/(A|De)sc/)[0] : 'Asc';
    console.log(sortOrder);
    let myTableJson = jsonFromHtml(myTableTbody, myTableThead);
    console.log(myTableJson);
    myTableJson.rows = customSort(myTableJson.rows, sortKey, sortOrder);
    arrayToTable(myTableJson.rows, myTableTbody, myTableThead);
});

function loadContent(id) {
    location.href = '/stocks/purchase/'+id;
}

function clickable(id){

    var row = document.getElementsByTagName('tr')
    row((e =>
    e.addEventListener(onclick(loadContent(id)))))
}

loadContent(id)
sortTable()