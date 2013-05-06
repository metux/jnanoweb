
EXECUTABLE=testserver
MAIN_CLASS=de.metux.nanoweb.example.srv
GCJ_ARGS=-rdynamic -fjni

JNI_HEADER=tmp/jni/net_sf_jpam_Pam.h
JNI_OBJECT=tmp/jni/pam_native.o
JNI_SOURCE=src/Pam.c

PAM_LIBS=-lpam

PREFIX?=/usr
SBINDIR?=$(PREFIX)/sbin

compile:	$(EXECUTABLE)

$(EXECUTABLE):	$(JNI_OBJECT)
	@echo "Building $@"
	@rm -Rf classes
	@mkdir -p classes
	@javac -d classes `find src -name "*.java"`
	@gcj $(GCJ_ARGS) -rdynamic -fjni `find src -name "*.java"` $(JNI_OBJECT) $(PAM_LIBS) -o $(EXECUTABLE) --main=$(MAIN_CLASS)

clean:
	@rm -Rf tmp classes $(JNI_OBJECT) $(JNI_HEADER)

run:	compile
	./$(EXECUTABLE)

policy:
	@for i in `find -name "*.java"` ; do \
		astyle --style=java --indent=tab --suffix=none --indent-switches < "$$i" > "$$i.tmp" 2>&1 | grep -ve "^unchanged" ; \
		mv "$$i.tmp" "$$i" ; \
	done

doc:
	@javadoc -d javadoc `find src -name "*.java"`

$(JNI_HEADER):
	@gcj -C `find src/net/sf/jpam/ -name "*.java"` -d tmp/jni
	@gcjh -classpath tmp/jni/net/sf/jpam -jni Pam -o $(JNI_HEADER)

$(JNI_OBJECT):		$(JNI_SOURCE) $(JNI_HEADER)
	@gcc -Itmp/jni $< -c -o $(JNI_OBJECT)

install:	$(EXECUTABLE)
	@mkdir -p $(DESTDIR)/$(SBINDIR)
	@cp $(EXECUTABLE) $(DESTDIR)/$(SBINDIR)
	@chmod u+x $(DESTDIR)/$(SBINDIR)/$(EXECUTABLE)
