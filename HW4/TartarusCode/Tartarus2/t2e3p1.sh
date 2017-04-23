for i in {41..50}
do
cp tartarus.ini t$i.ini
java -cp "../:." Tartarus t$i
rm t$i.ini
done
