export let ServerName, PrintDebug;

if (process.env.NODE_ENV === 'production') {
  ServerName = 'ancientaliens.ddns.net:8080';
  PrintDebug = false;
} else {
  //ServerName = 'localhost:8080';
  ServerName = 'ancientaliens.ddns.net:8080';
  PrintDebug = true;
}
