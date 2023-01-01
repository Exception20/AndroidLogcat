package com.example.android;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/* 
 * @Date 2023-01-01 15:00 +08:00
 */

public final class Logcat {

    private final List<String> command;

    private final Process process;


    private Logcat(ProcessBuilder pb) throws IOException {
        this.command = pb.command();
        this.process = pb.start();
    }

    private Logcat(String[] command) throws IOException {
        this(new ProcessBuilder(command));
    }

    private Logcat(List<String> command) throws IOException {
        this(new ProcessBuilder(command));
    }


    public static void clear() throws IOException {
        String[] command = { "logcat", "--clear" };
        new Logcat(command);
    }

    public static Result<Logcat> logTo(File file) {
        return logToFile(file.getPath());
    }

    public static Result<Logcat> logToFile(String path) {
        if (path == null || path.trim().length() == 0) {
            return Result.error(new IllegalArgumentException(path));
        }
        String[] command = { "logcat", "--file", path };
        Logcat logcat;
        try {
            logcat = new Logcat(command);
        } catch (IOException e) {
            return Result.error(e);
        }
        return Result.result(logcat);
    }


    public final Result<File> getLogFile() {
        int index = command.indexOf("--file");
        if (index < 0 || index+1 > command.size()-1) {
            return Result.error(new NoSuchElementException());
        } else {
            String path = command.get(index+1);
            return Result.result(new File(path));
        }
    }

    public final Process getProcess() {
        return process;
    }


    public static enum Buffer {
        DEFAULT,
        ALL,
        MAIN,
        SYSTEM,
        CRASH,
        RADIO,
        EVENTS;

        public String toCommandString() {
            return name().toLowerCase();
        }

    }


    public static final class Builder {

        private String file;

        private Integer rotateKBytes;

        private Integer rotateCount;

        private String format;

        private String regex;

        private Integer maxCount;

        private String buffer;

        private String filterspecs;


        public Builder file(File file) {
            this.file = file.getPath();
            return this;
        }

        public Builder file(CharSequence path) {
            this.file = path.toString();
            return this;
        }

        public Builder rotateKBytes(int kbytes) {
            this.rotateKBytes = kbytes;
            return this;
        }

        public Builder rotateCount(int count) {
            this.rotateCount = count;
            return this;
        }

        public Builder id(String id) {
            throw new UnsupportedOperationException("What this option ?");
        }

        public Builder format(String format) {
            this.format = format;
            return this;
        }

        public Builder regex(String expr) {
            this.regex = expr;
            return this;
        }

        public Builder maxCount(int count) {
            this.maxCount = count;
            return this;
        }

        public Builder buffer(Buffer... buffer) {
            if (buffer.length != 0) {
                StringBuilder sb = new StringBuilder();
                for (Buffer b : buffer) {
                    sb.append(b.toCommandString());
                    sb.append(',');
                }
                int len = sb.length();
                sb.delete(len-1, len);
                this.buffer = sb.toString();
            }
            return this;
        }

        public Builder filterspecs(String filterspecs) {
            this.filterspecs = filterspecs;
            return this;
        }

        public Result<Logcat> logcat() {
            List<String> command = new ArrayList<>();
            command.add("logcat");
            if (this.file != null) {
                command.add("--file");
                command.add(this.file);
                if (this.rotateKBytes != null) {
                    command.add("--rotate-kbytes");
                    command.add(this.rotateKBytes.toString());
                }
            }
            if (this.rotateCount != null) {
                command.add("--rotate-count");
                command.add(this.rotateCount.toString());
            }
            if (this.format != null) {
                command.add("--format");
                command.add(this.format);
            }
            if (this.regex != null) {
                command.add("--regex");
                command.add(this.regex);
            }
            if (this.maxCount != null) {
                command.add("--max-count");
                command.add(this.maxCount.toString());
            }
            if (this.buffer != null) {
                command.add("--buffer");
                command.add(this.buffer);
            }
            if (this.filterspecs != null) {
                command.add(this.filterspecs);
            }

            Logcat logcat;
            try {
                logcat = new Logcat(command);
            } catch (IOException e) {
                return Result.error(e);
            }
            return Result.result(logcat);
        }

    }

}
