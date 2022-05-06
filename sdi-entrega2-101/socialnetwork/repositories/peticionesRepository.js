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
            return peticiones;
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
            return result.insertedId;
        } catch (error) {
            throw (error);
        }
    }
};