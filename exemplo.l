/* Exemplo de Programa na linguagem L */

int n[0], max, i;

 /*Programa Principal*/

  for i<-0 to 2 step 4 do begin
    write("Digite um numero: ");
    readln(max);
  end
  if n[0] > n[1] then begin
     if n[0] > n[2] then begin max<-n[0];x<-1; end else max<-n[2];
  end else if n[1] > n[2] then max<-n[1]; else max<-n[2];
  writeln("O maior numero e' ",max);
