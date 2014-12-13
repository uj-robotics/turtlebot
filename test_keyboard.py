import sys
import termios
import contextlib
import socket
import sys

host = '192.168.0.110'
port = 50000
size = 1024
print "Movement\nu i o \nj k l \nm , .\nspeed q/z\nlinear speed w/x\nangular speed e/c\nSTOP-> SPACE KEY/K"



@contextlib.contextmanager
def raw_mode(file):
    old_attrs = termios.tcgetattr(file.fileno())
    new_attrs = old_attrs[:]
    new_attrs[3] = new_attrs[3] & ~(termios.ECHO | termios.ICANON)
    try:
        termios.tcsetattr(file.fileno(), termios.TCSADRAIN, new_attrs)
        yield
    finally:
        termios.tcsetattr(file.fileno(), termios.TCSADRAIN, old_attrs)


def main():
    print 'exit with ^C or ^D'
    with raw_mode(sys.stdin):
        try:
            while True:
                ch = sys.stdin.read(1)
                if not ch or ch == chr(4):
                    break
                s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                s.connect((host,port))
                c = ch
                s.send(c)
                data = s.recv(size)
                s.close()
                print 'Received:', data

        except (KeyboardInterrupt, EOFError):
            pass


if __name__ == '__main__':
    main()
