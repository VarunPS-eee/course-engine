const http = require('http');

const TOTAL_REQUESTS = 20; // Fire 20 parallel requests simultaneously
const options = {
    hostname: 'localhost',
    port: 8080,
    path: '/api/v1/courses/JAVA-101/enroll',
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    }
};

let statusCounts = {
    200: 0, // Successful enrollments
    409: 0, // Optimistic Lock Conflicts
    429: 0, // Rate Limited
    other: 0
};

let completed = 0;

console.log(`🚀 Launching ${TOTAL_REQUESTS} concurrent requests to /enroll...\n`);

for (let i = 1; i <= TOTAL_REQUESTS; i++) {
    const req = http.request(options, (res) => {
        if (res.statusCode === 200) statusCounts[200]++;
        else if (res.statusCode === 409) statusCounts[409]++;
        else if (res.statusCode === 429) statusCounts[429]++;
        else statusCounts.other++;

        completed++;
        if (completed === TOTAL_REQUESTS) {
            printResults();
        }
    });

    req.on('error', (err) => {
        statusCounts.other++;
        completed++;
        if (completed === TOTAL_REQUESTS) printResults();
    });

    req.end(); // Dispatch immediately
}

function printResults() {
    console.log('================ LOAD TEST METRICS ================');
    console.log(`✅ 200 OK (Success)           : ${statusCounts[200]}`);
    console.log(`⚔️ 409 Conflict (Lock Fail)    : ${statusCounts[409]}`);
    console.log(`🛑 429 Rate Limited (Blocked) : ${statusCounts[429]}`);
    console.log(`❌ Other Errors              : ${statusCounts.other}`);
    console.log('===================================================\n');
}