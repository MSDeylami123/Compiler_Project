document.getElementById('submitBtn').onclick = async function() {
    const code1 = document.getElementById('code1').value;
    const code2 = document.getElementById('code2').value;

    if (!code1.trim() || !code2.trim()) {
        alert('Both code boxes must be filled');
        return;
    }

    try {
        const res = await fetch('http://localhost:8080/analyze', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({code1, code2})
        });

        const text = await res.text();
        document.getElementById('output').innerText = text;
        document.getElementById('resultBox').classList.remove('hidden');

    } catch (e) {
        alert('Error: ' + e.message);
    }
};