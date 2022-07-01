<?php // content="text/plain; charset=utf-8"
require_once ('./src/jpgraph.php');
require_once ('./src/jpgraph_line.php');

//---------------------------------------------
//Doc File
			$file1 = "./filenhietdo.txt";
			$file2 = "./filedoam.txt";
			$file1s = file_get_contents($file1);
			$file2s = file_get_contents($file2);
			$line1s = explode("\n", $file1s);
			
			foreach($line1s as $newline1[]) {
			}
			
			$line2s = explode("\n", $file2s);
			
			foreach($line2s as $newline2[]) {
			}
			
//----------------------------------------------
//Ep kieu Du lieu nhiet do
	$nd0 = (int)$newline1[0];
	$nd1 = (int)$newline1[1];
	$nd2 = (int)$newline1[2];
	$nd3 = (int)$newline1[3];
	$nd4 = (int)$newline1[4];
	$nd5 = (int)$newline1[5];
	$nd6 = (int)$newline1[6];
	$nd7 = (int)$newline1[7];
	$nd8 = (int)$newline1[8];
	$nd9 = (int)$newline1[9];
	$nd10 = (int)$newline1[10];
	$nd11 = (int)$newline1[11];
	//12h
	$nd12 = (int)$newline1[12];
	$nd13 = (int)$newline1[13];
	$nd14 = (int)$newline1[14];
	$nd15 = (int)$newline1[15];
	$nd16 = (int)$newline1[16];
	$nd17 = (int)$newline1[17];
	$nd18 = (int)$newline1[18];
	$nd19 = (int)$newline1[19];
	$nd20 = (int)$newline1[20];
	$nd21 = (int)$newline1[21];
	$nd22 = (int)$newline1[22];
	$nd23 = (int)$newline1[23];
//Ep kieu Du lieu do am
	$da0 = (int)$newline2[0];
	$da1 = (int)$newline2[1];
	$da2 = (int)$newline2[2];
	$da3 = (int)$newline2[3];
	$da4 = (int)$newline2[4];
	$da5 = (int)$newline2[5];
	$da6 = (int)$newline2[6];
	$da7 = (int)$newline2[7];
	$da8 = (int)$newline2[8];
	$da9 = (int)$newline2[9];
	$da10 = (int)$newline2[10];
	$da11 = (int)$newline2[11];
	//12h
	$da12 = (int)$newline2[12];
	$da13 = (int)$newline2[13];
	$da14 = (int)$newline2[14];
	$da15 = (int)$newline2[15];
	$da16 = (int)$newline2[16];
	$da17 = (int)$newline2[17];
	$da18 = (int)$newline2[18];
	$da19 = (int)$newline2[19];
	$da20 = (int)$newline2[20];
	$da21 = (int)$newline2[21];
	$da22 = (int)$newline2[22];
	$da23 = (int)$newline2[23];
//-------------------------------------------
$datay1 = array($nd0,$nd1,$nd2,$nd3,$nd4,$nd5,$nd6,$nd7,$nd8,$nd9,$nd10,$nd11
		,$nd12,$nd13,$nd14,$nd15,$nd16,$nd17,$nd18,$nd19,$nd20,$nd21,$nd22,$nd23);
$datay2 = array($da0,$da1,$da2,$da3,$da4,$da5,$da6,$da7,$da8,$da9,$da10,$da11
		,$da12,$da13,$da14,$da15,$da16,$da17,$da18,$da19,$da20,$da21,$da22,$da23);

// Setup the graph
$graph = new Graph(2180,1080);
$graph->SetScale("textlin");

$theme_class=new UniversalTheme;

$graph->SetTheme($theme_class);
$graph->img->SetAntiAliasing(false);
$graph->title->SetColor('red:1');
$graph->title->SetFont(FF_ARIAL, FS_BOLD, 24);
$timestamp = time();
$graph->title->Set("Biểu đồ Nhiệt độ & Độ ẩm -- " . "Ngày: ". date("d / m / Y", $timestamp));
$graph->SetBox(false);

$graph->SetMargin(40,20,36,63);

$graph->img->SetAntiAliasing();

//Set truc Y
$graph->yaxis->HideZeroLabel();
$graph->yaxis->HideLine(false);
$graph->yaxis->HideTicks(false,false);
$graph->yaxis->SetFont(FF_ARIAL, FS_BOLD, 14);

//Set truc X
$graph->xgrid->Show();
$graph->xgrid->SetLineStyle("solid");
$graph->xaxis->SetTickLabels(array('00h','01h','02h','03h','04h','05h','06h','07h','08h','09h','10h','11h'
								,'12h','13h','14h','15h','16h','17h','18h','19h','20h','21h','22h','23h'));
$graph->xgrid->SetColor('#E3E3E3');
$graph->xaxis->SetFont(FF_ARIAL, FS_BOLD, 14);


// Tao line 1(Nhiet do)
$p1 = new LinePlot($datay1);
$graph->Add($p1);
$p1->SetColor("#6495ED");
$p1->SetLegend('Nhiệt độ: °C');

// Tao line 2(Do am)
$p2 = new LinePlot($datay2);
$graph->Add($p2);
$p2->SetColor("#FF1493");
$p2->SetLegend('Độ ẩm: %');

//$graph->legend->SetFrameWeight(1);
$graph->legend->Pos(0.43,0.97);
$graph->legend->SetFont(FF_ARIAL, FS_BOLD, 14);

// Output line
$graph->Stroke();

?>