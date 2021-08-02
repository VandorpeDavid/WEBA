export interface OrderItem {
    name: string;
    amount: number;
    pricePerItem: number;
}

export interface Order {
    id: string;
    completedByMe: boolean;
    created: Date;
    status: string;
    table: string;
    items: OrderItem[];
}