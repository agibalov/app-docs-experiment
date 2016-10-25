import {beforeMethod, Metadata, afterMethod} from "aspect.js";
import {Injector} from "@angular/core";
import {TransactionRecorder} from "./transaction-recorder";

export class LoggingAspect {
    @beforeMethod({
        classNamePattern: /.+/,
        methodNamePattern: /^(?!_).+$/
    })
    logBefore(metadata: Metadata): void {
        const injector: Injector = metadata.woveMetadata.injector;
        const transactionRecorder: TransactionRecorder = injector.get(TransactionRecorder);

        const className: string = metadata.className;
        const methodName: string = metadata.method.name;
        transactionRecorder.handleBefore(className, methodName);
    }

    @afterMethod({
        classNamePattern: /.+/,
        methodNamePattern: /^(?!_).+$/
    })
    async logAfter(metadata: Metadata): Promise<void> {
        const methodResult = metadata.method.result;
        if(methodResult instanceof Promise) {
            await methodResult;
        }

        const injector: Injector = metadata.woveMetadata.injector;
        const transactionRecorder: TransactionRecorder = injector.get(TransactionRecorder);

        const className: string = metadata.className;
        const methodName: string = metadata.method.name;
        transactionRecorder.handleAfter(className, methodName);
    }
}
