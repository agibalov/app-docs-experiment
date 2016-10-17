module.exports = {
    entry: './src/main.ts',
    output: {
        path: __dirname + '/dist',
        filename: 'bundle.js'
    },
    resolve: {
        extensions: ['', '.ts', '.js']
    },
    module: {
        loaders: [
            {
                test: /\.ts$/,
                loader: 'babel?presets[]=es2015!ts',
                exclude: /node_modules/
            }
        ]
    },
    devServer: {
        proxy: {
            '/api': {
                target: 'http://localhost:8080/',
                secure: false
            }
        }
    }
}
