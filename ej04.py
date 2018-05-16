import io
import re
from base64 import b64encode, b64decode
from nacl.pwhash import verify
from hashlib import sha512

with io.open('common-passwords.txt', mode = 'r', encoding = 'latin-1') as f:
  passwords = f.read().splitlines()

print (b64encode(sha512(b'password4ZBpg18p').digest()))

# Passwords

with io.open('passwords.txt', mode = 'r') as f:
  passwordstxt = f.read().splitlines()

m = map(lambda x: re.search(r'^(?P<name>usuario\d+), \$(?P<salt>.+)\$(?P<hash>.*)$', x), passwordstxt)
l = list(map(lambda x: (x.group('name'), x.group('salt'), x.group('hash')), m))

with io.open('contraseñas.txt', mode ='w', encoding = 'utf-8') as f:
  for (user,salt,hash) in l:
    for p in passwords:
      if sha512((p+salt).encode()).digest() == b64decode(hash):
        f.write('%s, %s\n' % (user, p))
        break

del l

# Argon2id

with io.open('passwords2.txt', mode = 'r') as f:
  passwords2txt = f.read().splitlines()

m = map(lambda x: re.search(r'^(?P<name>usuario\w); (?P<argon2>.*)$', x), passwords2txt)
l = list(map(lambda x: (x.group('name'), x.group('argon2')), m))

with io.open('contraseñas2.txt', mode ='w', encoding = 'utf-8') as f:
  for (user,hash) in l:
    for p in passwords:
      try:
        if verify(hash.encode(), p.encode()):
          f.write('%s, %s\n' % (user,p))
          break
      except: pass
