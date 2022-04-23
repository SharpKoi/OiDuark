#!/bin/bash

echo "Start building new release...";

version=$(xmllint --xpath "//*[local-name()='properties']/*[local-name()='app.version']/text()" pom.xml);
release_path="release/oiduark-$version";
launcher_path="$release_path/bin/oiduark-launcher";

echo "Version: $version";

echo "Running maven package...";
mvn clean install -f pom.xml;

echo "Copying release files...";
if [ ! -d $release_path ]; then
    mkdir -p $release_path;
fi

cp -f -r target/jlink-image/* "$release_path/";
cp -f target/oiduark-$version.jar "$release_path/bin/oiduark.jar";

echo "Creating launcher...";
if [ ! -f $launcher_path ]; then
    echo "#!/bin/bash" >> "$launcher_path.sh";
    echo "" >> "$launcher_path.sh";
    echo "./java -jar ./oiduark.jar" >> "$launcher_path.sh";
fi

shc -f "$launcher_path.sh" -o $launcher_path;

echo "Cleaning release space...";
rm -f "$launcher_path.sh" "$launcher_path.sh.x.c";

echo "Done!";
