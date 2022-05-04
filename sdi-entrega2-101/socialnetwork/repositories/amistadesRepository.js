module.exports = {
    mongoClient: null,
    app: null,
    init: function (app, mongoClient) {
        this.mongoClient = mongoClient;
        this.app = app;
    },
    findAmistadesByEmail: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("socialNetwork");
            const collectionName = 'amistades';
            const amistadesCollection = database.collection(collectionName);
            const amistades = await amistadesCollection.find(filter, options).toArray();
            return amistades;
        } catch (error) {
            throw (error);
        }
    },
    insertAmistad: async function (amistad,options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("socialNetwork");
            const collectionName = 'amistades';
            const usersCollection = database.collection(collectionName);
            const result = await usersCollection.insertOne(amistad);
            return result.insertedId;
        } catch (error) {
            throw (error);
        }
    }
};