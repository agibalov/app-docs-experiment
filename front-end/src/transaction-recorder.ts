import {Injectable} from "@angular/core";
import {BETransactionRecord} from "./be-transaction-record";

@Injectable()
export class TransactionRecorder {
    handleBefore(className: string, methodName: string): void {
        console.log(`FE BEFORE ${className}::${methodName}`);
    }

    handleAfter(className: string, methodName: string): void {
        console.log(`FE AFTER ${className}::${methodName}`);
    }

    handleBELog(beTransactionRecords: BETransactionRecord[]): void {
        for(const record of beTransactionRecords) {
            console.log(`BE ${record.comment} ${record.className}::${record.methodName}`);
        };
    }
}
