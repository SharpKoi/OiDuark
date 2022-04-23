#!/bin/bash

echo "Start building new release...";

appname=$(xmllint --xpath "//*[local-name()='properties']/*[local-name()='app.name']/text()" pom.xml);
version=$(xmllint --xpath "//*[local-name()='properties']/*[local-name()='app.version']/text()" pom.xml);
release_dir="release"
release_path="$release_dir/$appname-$version";
launcher_path="$release_path/bin/$appname-launcher";

echo "Version: $version";

echo "Running maven package...";
mvn clean install -f pom.xml;

echo "Copying release files...";
if [ ! -d $release_path ]; then
    mkdir -p $release_path;
fi

cp -f -r target/jlink-image/* "$release_path/";
cp -f target/$appname-$version.jar "$release_path/bin/$appname.jar";

echo "Creating launcher...";
if [ ! -f $launcher_path ]; then
    echo "#!/bin/bash" >> "$launcher_path.sh";
    echo "" >> "$launcher_path.sh";
    echo "./java -jar ./$appname.jar" >> "$launcher_path.sh";
fi

shc -f "$launcher_path.sh" -o $launcher_path;

echo "Cleaning release space...";
rm -f "$launcher_path.sh" "$launcher_path.sh.x.c";

echo "compressing release...";
cd $release_dir;
tar -czvf "$appname-$version.tar.gz" *;

echo "Done!";
