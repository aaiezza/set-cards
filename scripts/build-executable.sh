#!/usr/bin/env bash

# Run after executing:
#  `mvn clean compile assembly:single`

cd $(dirname "$0")/..

echo "Making executable..."

mkdir -p target
touch target/set.sh

if [ -e target/set-cards.jar ]; then
  echo """#!/usr/bin/env bash

MYSELF=\`which \"\$0\" 2>/dev/null\`
[ $? -gt 0 -a -f \"\$0\" ] && MYSELF=\"./\$0\"
java=java
if test -n \"\$JAVA_HOME\"; then
    java=\"\$JAVA_HOME/bin/java\"
fi
exec \"\$java\" \$java_args -jar \$MYSELF \"\$@\"
exit 1
""" >target/set.sh

  cat target/set-cards.jar >>target/set.sh

else
  echo """#!/usr/bin/env bash

echo Nothing to run
""" >target/set.sh
fi

chmod +x target/set.sh
