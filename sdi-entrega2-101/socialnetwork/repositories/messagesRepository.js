module.exports = {
    mongoClient: null,
    app: null,
    init: function (app, mongoClient) {
        this.mongoClient = mongoClient;
        this.app = app;
    },
    findMessage: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("socialNetwork");
            const collectionName = 'messages';
            const usersCollection = database.collection(collectionName);
            const user = await usersCollection.findOne(filter, options);
            client.close();
            return user;
        } catch (error) {
            throw (error);
        }
    },
    findMessages: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("socialNetwork");
            const publicationsCollection = database.collection('messages');
            const publications = await publicationsCollection.find(filter, options).toArray();
            client.close();
            return publications;
        } catch (error) {
            throw (error);
        }
    },
    insertMessage: async function (message) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("socialNetwork");
            const collectionName = 'messages';
            const usersCollection = database.collection(collectionName);
            const result = await usersCollection.insertOne(message);
            client.close();
            return result.insertedId;
        } catch (error) {
            throw (error);
        }
    },
    updateMessage: async function(newMessage, filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("socialNetwork");
            const collectionName = 'messages';
            const songsCollection = database.collection(collectionName);
            const result = await songsCollection.updateOne(filter, {$set: newMessage}, options);
            client.close();
            return result;
        } catch (error) {
            throw (error);
        }
    },
    deleteMessage: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("socialNetwork");
            const collectionName = 'messages';
            const songsCollection = database.collection(collectionName);
            const result = await songsCollection.deleteOne(filter, options);
            client.close();
            return result;
        } catch (error) {
            throw (error);
        }
    },
};