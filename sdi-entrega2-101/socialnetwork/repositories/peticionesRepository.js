module.exports = {
    mongoClient: null,
    app: null,
    init: function (app, mongoClient) {
        this.mongoClient = mongoClient;
        this.app = app;
    },
    findPeticionesByEmail: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("socialNetwork");
            const collectionName = 'peticiones';
            const peticionesCollection = database.collection(collectionName);
            const peticiones = await peticionesCollection.find(filter, options).toArray();
            client.close();
            return peticiones;
        } catch (error) {
            throw (error);
        }
    },
    getPeticionesPg: async function (filter, options, page) {
        try {
            const limit = 4;
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("socialNetwork");
            const collectionName = 'peticiones';
            const peticionesCollection = database.collection(collectionName);
            const counter = peticionesCollection.find(filter, options);
            const cursor = counter.skip((page - 1) * limit).limit(limit);
            const peticiones = await cursor.toArray();
            const peticionesCollectionCount = await peticionesCollection.find(filter, options).toArray();
            const result = {peticiones: peticiones, total: peticionesCollectionCount.length};
            client.close();
            return result;
        } catch (error) {
            throw (error);
        }
    },
    deletePeticion: async function (id) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("socialNetwork");
            const collectionName = 'peticiones';
            const peticionesCollection = database.collection(collectionName);
            const result = await peticionesCollection.deleteOne(id);
            client.close();
            return result.insertedId;
        } catch (error) {
            throw (error);
        }
    },
    deletePeticiones: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("socialNetwork");
            const collectionName = 'peticiones';
            const peticionesCollection = database.collection(collectionName);
            const result = await peticionesCollection.remove(filter,options);
            client.close();
            return result;
        } catch (error) {
            throw (error);
        }
    },
    insertPeticion: async function (peticion,options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("socialNetwork");
            const collectionName = 'peticiones';
            const peticionesCollection = database.collection(collectionName);
            const result = await peticionesCollection.insertOne(peticion);
            client.close();
            return result.insertedId;
        } catch (error) {
            throw (error);
        }
    }
};