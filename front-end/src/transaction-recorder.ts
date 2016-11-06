import {Injectable} from "@angular/core";
import {TransactionEvent, TransactionEventType} from "./transaction-event";

@Injectable()
export class TransactionRecorder {
    private transactionEvents: TransactionEvent[] = [];

    constructor() {
        // TODO: is it reliable way to expose?
        (<any>window).transactionRecorder = this;
    }

    handleBefore(className: string, methodName: string): void {
        const event: TransactionEvent = {
            tag: "FE",
            comment: '',
            eventType: TransactionEventType.Enter,
            className: className,
            methodName: methodName
        };
        this.dumpEvent(event);
        this.transactionEvents.push(event);
    }

    handleAfter(className: string, methodName: string): void {
        const event: TransactionEvent = {
            tag: "FE",
            comment: '',
            eventType: TransactionEventType.Leave,
            className: className,
            methodName: methodName
        };
        this.dumpEvent(event);
        this.transactionEvents.push(event);
    }

    appendEvents(events: TransactionEvent[]): void {
        events.forEach((event) => {
            this.dumpEvent(event);
            this.transactionEvents.push(event);
        });
    }

    private dumpEvent(event: TransactionEvent): void {
        console.log(`${event.tag} ${event.eventType} ${event.className}::${event.methodName}`);
    }

    reset(): void {
        this.transactionEvents = [];
    }

    getTransactionEvents(): TransactionEvent[] {
        return this.transactionEvents;
    }

    getTransactionEventsJson(): string {
        return JSON.stringify({
            events: this.transactionEvents
        });
    }
}
