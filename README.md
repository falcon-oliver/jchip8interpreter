# Chip8 Interpreter
<p align="center">


<img src="https://github.com/user-attachments/assets/c67b275d-7eb0-42cf-8b64-4f4bafe69155">

</p>


## Installation
### From release

Download <a href="https://github.com/falcon-oliver/jchip8interpreter/releases/download/1.0/app.jar">here</a>

```bash
java -jar app.jar
```

### From source
```bash
git clone https://github.com/0xRobinman/Chip8-interpreter
cd Chip8-interpreter
gradle build
gradle run
```

## Tests conducted

Using a chip8 <a href="https://github.com/Timendus/chip8-test-suite">test suite</a> we conducted tests against the interpreter. Seen below is the tests in a passing state.

### Opcode test

![image](https://github.com/user-attachments/assets/6c2a8e2c-c08a-4332-91f3-d62559529876)

### Flags test

![image](https://github.com/user-attachments/assets/ae5498bd-7369-4808-b703-3e8b49a97c9d)

## Resources I found useful

1. https://en.wikipedia.org/wiki/CHIP-8
2. https://www.cs.columbia.edu/~sedwards/classes/2016/4840-spring/designs/Chip8.pdf
3. https://github.com/Timendus/chip8-test-suite
