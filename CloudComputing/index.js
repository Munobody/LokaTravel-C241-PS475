// This private endpoint API for authentication was deployed by CC team Lokatravel 

const express = require('express');
const bodyParser = require('body-parser');
const { Datastore } = require('@google-cloud/datastore');
const bcrypt = require('bcrypt');
const cors = require('cors'); // Import cors module

const app = express();
app.use(bodyParser.json());
app.use(cors()); // Use cors middleware

const datastore = new Datastore();

// Endpoint for user signup
app.post('/signup', async (req, res) => {
    const { fullname, email, password, confirmPassword } = req.body;
    console.log('Received signup request:', req.body);  // Logging input

    // Validasi input
    if (!fullname || !email || !password || !confirmPassword) {
        return res.status(400).send({ message: 'All fields are required' });
    }

    if (password !== confirmPassword) {
        return res.status(400).send({ message: 'Passwords do not match' });
    }

    const userKey = datastore.key(['User', email]);

    try {
        // Password encryption
        const hashedPassword = await bcrypt.hash(password, 10);
        console.log('Hashed password:', hashedPassword);  // Logging hashed password

        const entity = {
            key: userKey,
            data: {
                fullname: fullname,
                email: email,
                password: hashedPassword,
            },
        };

        await datastore.save(entity);
        console.log('User saved successfully');  // Logging success
        return res.status(201).send({ message: 'User created successfully' });
    } catch (error) {
        console.error('Error saving user:', error);  // Logging error
        return res.status(500).send({ message: error.message });
    }
});

// Endpoint for user login
app.post('/login', async (req, res) => {
    const { email, password } = req.body;
    console.log('Received login request:', req.body);  // Logging input

    // Input Validation
    if (!email || !password) {
        return res.status(400).send({ message: 'Email and password are required' });
    }

    const userKey = datastore.key(['User', email]);

    try {
        const [user] = await datastore.get(userKey);

        // Password verification
        if (!user || !(await bcrypt.compare(password, user.password))) {
            console.log('Invalid email or password');  // Logging invalid login
            return res.status(401).send({ message: 'Invalid email or password' });
        }

        console.log('Login successful');  // Logging success
        return res.status(200).send({ message: 'Login successful' });
    } catch (error) {
        console.error('Error logging in user:', error);  // Logging error
        return res.status(500).send({ message: error.message });
    }
});

// Server running
const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});