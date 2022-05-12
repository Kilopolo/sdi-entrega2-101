module.exports = {
    mongoClient: null,
    app: null,
    init: function (app, mongoClient) {
        this.mongoClient = mongoClient;
        this.app = app;
    },
    findUser: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("socialNetwork");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            const user = await usersCollection.findOne(filter, options);
            client.close();
            return user;
        } catch (error) {
            throw (error);
        }
    },
    getUsersPage:  async function (filter, options,page,limit) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("socialNetwork");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            const cursor = usersCollection.find(filter, options).
            skip((page - 1) * limit).limit(limit)
            const UsersCollectionCount = await usersCollection.find(filter, options).count();
            const users = await cursor.toArray();
            const result = {users: users, total: UsersCollectionCount};
            client.close();
            return result;
        } catch (error) {
            throw (error);
        }
    },
    findUsers: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("socialNetwork");
            const usersCollection = database.collection('users');
            const result = await usersCollection.find(filter, options).sort({"email":1}).toArray();
            client.close();
            return result;
        } catch (error) {
            throw (error);
        }
    },
    deleteUsers: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("socialNetwork");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            const result = await usersCollection.remove(filter,options);
            client.close();
            return result;
        } catch (error) {
            throw (error);
        }
    },
    insertUser: async function (user) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("socialNetwork");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            const result = await usersCollection.insertOne(user);
            client.close();
            return result.insertedId;
        } catch (error) {
            throw (error);
        }
    }
};