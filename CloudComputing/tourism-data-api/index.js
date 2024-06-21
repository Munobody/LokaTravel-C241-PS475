const express = require('express');
const { BigQuery } = require('@google-cloud/bigquery');
const app = express();
const cors = require('cors'); 
const port = process.env.PORT || 8080;

app.use(cors());  

const bigquery = new BigQuery();

app.get('/places/:city', async (req, res) => {
    const city = req.params.city;
    console.log(`Request received for city: ${city}`);  
    const query = `
        SELECT Place_Id, Place_Name, Category, City, Rating, Lat, Long
        FROM \`capstone-project-c241-ps475.tourism_data_lokatravel.tourism_new\`
        WHERE City = @city
    `;
    const options = {
        query: query,
        location: 'asia-southeast2',  
        params: { city: city }
    };

    try {
        const [rows] = await bigquery.query(options);
        console.log(`Query successful, returning ${rows.length} results`);  
        res.status(200).json(rows);
    } catch (error) {
        console.error(error);
        res.status(500).send('Internal Server Error');
    }
});

app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
});
