
EXECUTABLE=testserver
MAIN_CLASS=de.metux.nanoweb.example.srv
GCJ_ARGS=

PREFIX?=/usr
SBINDIR?=$(PREFIX)/sbin

compile:	$(EXECUTABLE)

$(EXECUTABLE):
	@rm -Rf classes
	@mkdir -p classes
	@javac -d classes `find src -name "*.java"`
	@gcj $(GCJ_ARGS) `find src -name "*.java"` -o $(EXECUTABLE) --main=$(MAIN_CLASS)

clean:
	@rm -Rf classes $(EXECUTABLE) tmp

run:	compile
	./$(EXECUTABLE)

policy:
	@for i in `find -name "*.java"` ; do \
		astyle --style=java --indent=tab --suffix=none --indent-switches < "$$i" > "$$i.tmp" 2>&1 | grep -ve "^unchanged" ; \
		mv "$$i.tmp" "$$i" ; \
	done

doc:
	@javadoc -d javadoc `find src -name "*.java"`

install:	$(EXECUTABLE)
	@mkdir -p $(DESTDIR)/$(SBINDIR)
	@cp $(EXECUTABLE) $(DESTDIR)/$(SBINDIR)
	@chmod u+x $(DESTDIR)/$(SBINDIR)/$(EXECUTABLE)
