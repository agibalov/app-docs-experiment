export enum TransactionEventType {
    Enter,
    Leave
};

export class TransactionEvent {
    tag: string;
    comment: string;
    eventType: TransactionEventType;
    className: string;
    methodName: string;
};
