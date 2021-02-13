let tableId = "mainTable";

const sleep = t => new Promise(s => setTimeout(s, t));

document.onreadystatechange = () => {
    if (document.readyState === 'complete') {
        // https://patdavid.net/2019/02/displaying-a-big-html-table/
        console.log("complete");
        document.getElementById(tableId).style.display = "table";
    }
};

async function removeLastColumn() {
    let rows = document.getElementById(tableId).rows;
    await new Promise(resolve => setTimeout(resolve, 3000));
    for (let i = 0; i < rows.length; i++) {
        for (let c = 0; c < rows[i].cells.length; c++) {
            if(c == rows[i].cells.length-1){
                rows[i].deleteCell(c);
            }
        }
    }
}

async function removeFirstColumn() {
    let rows = document.getElementById(tableId).rows;
    await new Promise(resolve => setTimeout(resolve, 3000));
    for (let i = 0; i < rows.length; i++) {
        rows[i].deleteCell(0);
    }
}

async function removeFirstRow() {
    await sleep(2000);
    let table = document.getElementById(tableId);
    let tbody = table.getElementsByTagName("TBODY")[0];
    let row = tbody.getElementsByTagName('tr')[0];
    console.log(row);
    if (table.rows.length > 1){
        tbody.removeChild(row);
    }
}
