from http.server import HTTPServer, BaseHTTPRequestHandler
import subprocess, os

class Handler(BaseHTTPRequestHandler):
    def do_OPTIONS(self):
        self.send_response(200)
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Access-Control-Allow-Methods', 'POST, OPTIONS')
        self.send_header('Access-Control-Allow-Headers', 'Content-Type')
        self.end_headers()

    def do_POST(self):
        if self.path == '/analyze':
            # Read codes
            length = int(self.headers['Content-Length'])
            data = self.rfile.read(length).decode()
            import json
            codes = json.loads(data)

            # Save to input1.txt and input2.txt
            with open('antlr_project/input1.txt', 'w') as f1:
                f1.write(codes['code1'])
            with open('antlr_project/input2.txt', 'w') as f2:
                f2.write(codes['code2'])

            # Run Java Main (it reads input1.txt and input2.txt)
            os.chdir('antlr_project')
            result = subprocess.run(['java', 'Main'],
                                  capture_output=True, text=True, timeout=10)
            os.chdir('..')

            # Send Java output back to browser
            self.send_response(200)
            self.send_header('Content-Type', 'text/plain')
            self.send_header('Access-Control-Allow-Origin', '*')
            self.end_headers()
            self.wfile.write(result.stdout.encode())

    def do_GET(self):
        if self.path in ['/', '/index.html']:
            with open('UI/index.html', 'rb') as f:
                self.send_response(200)
                self.send_header('Content-type', 'text/html')
                self.send_header('Access-Control-Allow-Origin', '*')
                self.end_headers()
                self.wfile.write(f.read())
        elif self.path == '/style.css':
            with open('UI/style.css', 'rb') as f:
                self.send_response(200)
                self.send_header('Content-type', 'text/css')
                self.send_header('Access-Control-Allow-Origin', '*')
                self.end_headers()
                self.wfile.write(f.read())
        elif self.path == '/script.js':
            with open('UI/script.js', 'rb') as f:
                self.send_response(200)
                self.send_header('Content-type', 'application/javascript')
                self.send_header('Access-Control-Allow-Origin', '*')
                self.end_headers()
                self.wfile.write(f.read())
        else:
            self.send_response(404)
            self.end_headers()

HTTPServer(('localhost', 8080), Handler).serve_forever()