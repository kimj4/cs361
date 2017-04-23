for i in {51..60}
do
cp tartarus.ini t$i.ini
java -cp "../:." Tartarus t$i
rm t$i.ini
done
