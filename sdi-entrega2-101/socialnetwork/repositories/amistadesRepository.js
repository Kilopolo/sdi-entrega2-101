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
    getAmistadesPg: async function (filter, options, page) {
        try {
            const limit = 4;
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("socialNetwork");
            const collectionName = 'amistades';
            const amistadesCollection = database.collection(collectionName);
            const counter = amistadesCollection.find(filter, options);
            const cursor = counter.skip((page - 1) * limit).limit(limit);
            const amistades = await cursor.toArray();
            const amistadesCollectionCount = await amistadesCollection.find(filter, options).toArray();
            const result = {amistades: amistades, total: amistadesCollectionCount.length};
            return result;
        } catch (error) {
            throw (error);
        }
    },
    findAmistad: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("socialNetwork");
            const collectionName = 'amistades';
            const usersCollection = database.collection(collectionName);
            const user = await usersCollection.findOne(filter, options);
            return user;
        } catch (error) {
            throw (error);
        }
    },
    findAmistades: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("socialNetwork");
            const publicationsCollection = database.collection('amistades');
            const publications = await publicationsCollection.find(filter, options).toArray();
            return publications;
        } catch (error) {
            throw (error);
        }
    },
    deleteAmistades: async function (filter,options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("socialNetwork");
            const collectionName = 'amistades';
            const amistadesCollection = database.collection(collectionName);
            const result = await amistadesCollection.remove(filter,options);
            return result;
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