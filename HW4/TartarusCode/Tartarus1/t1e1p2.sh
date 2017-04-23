for i in {11..20}
do
cp tartarus.ini t$i.ini
java -cp "../:." Tartarus t$i
rm t$i.ini
done
