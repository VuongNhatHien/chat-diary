export interface ChatRoom {
  id: string;
  name: string;
  userId: string;
  createdAt: string;
}

export interface ChatMessage {
  id: number;
  userId: string;
  roomId: string;
  text: string;
}