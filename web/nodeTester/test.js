function get_status()
{
    let url = get_address() + "/status"

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
    let url = get_address() + "/close"

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

function send_file_action() {
    let fc = document.getElementById("fileChooser");
    if(fc.files.length > 0) {
        send_file(fc.files[0])
    } else {
        alert("Please select file")
    }
}

function send_file(file) {
    let url = get_address() + "/upload"

    fetch(url, {
        method: 'POST',
        headers: {
            "Content-Type": "application/octet-stream"
        },
        body: file
    })
    .then(function(response) {
        return response.json()
    })
    .then(function(json) {
        console.log(json)
    })
    .catch(function(error) {
        console.log("error: " + error)
    })

}

function get_address() {
    addressBox = document.getElementById("addressBox")
    return "http://" + addressBox.value
}
