import { CodeModel, readCode } from "./code-model-reader";
import * as fs from 'fs';
import * as glob from 'glob';

glob('src/**/*.ts', function(err, files) {
    const codeModel: CodeModel = readCode(files);

    fs.writeFile(
        'dist/code-model.json',
        JSON.stringify(codeModel, null, '  '));
});
