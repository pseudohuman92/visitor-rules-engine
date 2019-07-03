export let ServerName, PrintDebug;

if (process.env.NODE_ENV === 'production') {
  ServerName = 'visitorccg.com:8080';
  PrintDebug = false;
} else {
  ServerName = 'localhost:8080';
  //ServerName = 'visitorccg.com:8080';
  PrintDebug = true;
}
