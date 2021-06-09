function get_status()
{
    
    addressBox = document.getElementById("addressBox")
    let url = "http://" + addressBox.value + "/status"

    let label = document.getElementById("status")
    label.textContent = "Status: "


    fetch(url)
    .then(function(response) {
        
        return response.json();
    })
    .then(function(json) {
        label.textContent = "Status: " + JSON.stringify(json)
        console.log(json)
    })
    .catch(function (error) {
        console.log("Error:" + error);
    })        
}


function close_server()
{
    addressBox = document.getElementById("addressBox")
    let url = "http://" + addressBox.value + "/close"

    let label = document.getElementById("status")
    label.textContent = "Status: "

         
    fetch(url)
    .then(function(response) {
        return response.json();
    })
    .then(function(json) {
        label.textContent = "Status: " + JSON.stringify(json)
    }) 
    .catch(function (error) {
        console.log("Error:" + error);
    })
}        
