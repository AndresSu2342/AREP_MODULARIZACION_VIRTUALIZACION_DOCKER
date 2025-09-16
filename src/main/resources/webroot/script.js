function testGreeting() {
    const name = document.getElementById("nameInput").value;
    fetch(`/greeting?name=${name}`)
        .then(response => response.text())
        .then(data => {
            document.getElementById("greetingResult").innerText = data;
        });
}

function testPi() {
    const decimals = document.getElementById("piDecimals").value;
    fetch(`/pi?decimals=${decimals}`)
        .then(response => response.text())
        .then(data => {
            document.getElementById("piResult").innerText = data;
        });
}

function testSum() {
    const numbers = document.getElementById("sumNumbers").value;
    fetch(`/sum?number=${numbers}`)
        .then(response => response.text())
        .then(data => {
            document.getElementById("sumResult").innerText = data;
        });
}
