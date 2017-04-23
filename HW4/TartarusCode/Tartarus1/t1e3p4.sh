for i in {71..80}
do
cp tartarus.ini t$i.ini
java -cp "../:." Tartarus t$i
rm t$i.ini
done
