import {Injectable} from "@angular/core";
import {BETransactionRecord} from "./be-transaction-record";

@Injectable()
export class TransactionRecorder {
    private records: string[] = [];

    constructor() {
        // TODO: is it reliable way to expose?
        (<any>window).transactionRecorder = this;
    }

    handleBefore(className: string, methodName: string): void {
        const recordString: string = `FE BEFORE ${className}::${methodName}`;
        console.log(recordString);
        this.records.push(recordString);
    }

    handleAfter(className: string, methodName: string): void {
        const recordString: string = `FE AFTER ${className}::${methodName}`;
        console.log(recordString);
        this.records.push(recordString);
    }

    handleBELog(beTransactionRecords: BETransactionRecord[]): void {
        for(const record of beTransactionRecords) {
            const recordString: string = `BE ${record.comment} ${record.className}::${record.methodName}`;
            console.log(record);
            this.records.push(recordString);
        };
    }

    reset(): void {
        this.records = [];
    }

    getRecords(): string[] {
        return this.records;
    }
}
