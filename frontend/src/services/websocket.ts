import { Client } from "@stomp/stompjs";
import type { IMessage } from "@stomp/stompjs";

let client: Client | null = null;

export function connectWebSocket(onMessage: (msg: any) => void) {

    client = new Client({
        brokerURL: "ws://localhost:8080/ws",
        reconnectDelay: 5000,

        onConnect: () => {
            client?.subscribe("/topic/albuns", (message: IMessage) => {
                onMessage(JSON.parse(message.body));
            });
        }
    });

    client.activate();
}
