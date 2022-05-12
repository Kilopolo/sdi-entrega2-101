module.exports = {
    mongoClient: null,
    app: null,

    init: function (app, mongoClient) {
        this.mongoClient = mongoClient;
        this.app = app;
    },

    findPublications: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("socialNetwork");
            const publicationsCollection = database.collection('publications');
            const publications = await publicationsCollection.find(filter, options).toArray();
            client.close();
            return publications;
        } catch (error) {
            throw (error);
        }
    },

    insertPublication: async function (publication) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("socialNetwork");
            const publicationsCollection = database.collection('publications');
            const result = await publicationsCollection.insertOne(publication);
            client.close();
            return result.insertedId;
        } catch (error) {
            throw (error);
        }
    }
};