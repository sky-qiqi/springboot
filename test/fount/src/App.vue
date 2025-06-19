<template>
  <div id="chat-container">
    <h1>AI Chat</h1>
    <div class="messages">
      <div v-for="(msg, index) in messages" :key="index" :class="['message', msg.sender]">
        {{ msg.text }}
      </div>
    </div>
    <div class="input-area">
      <input type="text" v-model="newMessage" @keyup.enter="sendMessage" placeholder="Type your message...">
      <button @click="sendMessage">Send</button>
    </div>
    <div v-if="error" class="error-message">{{ error }}</div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';

const messages = ref([]);
const newMessage = ref('');
const error = ref('');

// Adjust the backend URL if your Spring Boot app runs on a different port
const backendUrl = 'http://localhost:8080/api/chat';

async function sendMessage() {
  const userMessage = newMessage.value.trim();
  if (!userMessage) return;

  messages.value.push({ sender: 'user', text: userMessage });
  newMessage.value = '';
  error.value = ''; // Clear previous errors

  try {
    const response = await axios.post(backendUrl, { message: userMessage });
    messages.value.push({ sender: 'ai', text: response.data.reply });
  } catch (err) {
    console.error('Error sending message:', err);
    error.value = 'Failed to get response from AI. Please check the backend connection.';
    // Optionally add a system message indicating the error
    messages.value.push({ sender: 'system', text: 'Error: Could not reach AI service.' });
  }
}
</script>

<style scoped>
#chat-container {
  max-width: 600px;
  margin: 20px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-family: sans-serif;
  display: flex;
  flex-direction: column;
  height: 80vh; /* Limit height */
}

h1 {
  text-align: center;
  margin-bottom: 20px;
}

.messages {
  flex-grow: 1;
  overflow-y: auto; /* Allow scrolling */
  border: 1px solid #eee;
  padding: 10px;
  margin-bottom: 10px;
  background-color: #f9f9f9;
}

.message {
  padding: 8px 12px;
  margin-bottom: 8px;
  border-radius: 5px;
  max-width: 80%;
}

.message.user {
  background-color: #d1eaff;
  align-self: flex-end;
  margin-left: auto; /* Push user messages to the right */
  text-align: right;
}

.message.ai {
  background-color: #e0e0e0;
  align-self: flex-start;
  margin-right: auto; /* Push AI messages to the left */
}

.message.system {
  background-color: #ffebee;
  color: #c62828;
  font-style: italic;
  text-align: center;
  margin: 5px auto;
}

.input-area {
  display: flex;
  margin-top: auto; /* Push input to the bottom */
}

.input-area input {
  flex-grow: 1;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-right: 10px;
}

.input-area button {
  padding: 10px 15px;
  border: none;
  background-color: #007bff;
  color: white;
  border-radius: 4px;
  cursor: pointer;
}

.input-area button:hover {
  background-color: #0056b3;
}

.error-message {
    color: red;
    margin-top: 10px;
    text-align: center;
}
</style>