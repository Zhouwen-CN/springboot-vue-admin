echo exit | sqlplus -s ROOT/123456@//localhost/XEPDB1 @/tmp/schema-oracle.sql
echo exit | sqlplus -s ROOT/123456@//localhost/XEPDB1 @/tmp/quartz-schema-oracle.sql
echo exit | sqlplus -s ROOT/123456@//localhost/XEPDB1 @/tmp/data-oracle.sql