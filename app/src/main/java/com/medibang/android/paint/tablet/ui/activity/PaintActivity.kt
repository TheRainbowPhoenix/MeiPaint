package com.medibang.android.paint.tablet.ui.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medibang.android.paint.tablet.ui.activity.ui.theme.MeiPaintTheme
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class PaintActivity : ComponentActivity() {
    private lateinit var canvasBitmap: Bitmap
    private val _canvasBitmap = mutableStateOf<ImageBitmap?>(null)
    val imageBitmap: ImageBitmap? by _canvasBitmap

    companion object {
        init {
            try {
                System.loadLibrary("NativeLib")
            } catch (unused: UnsatisfiedLinkError) {
                // Log the error using Android Log
                Log.e("MeiPaint", "Error loading native library: ${unused.message}")
            }
        }
    }

    // Declare the native method
    private external fun test(): Int

    // neet::CMangaEngine::Initialize(int, int, neet::TLayerType) /size?, size?, neet::TLayerType
    external fun initialize(width: Int, height: Int, tLayerType: Int)

    external fun nAddGradPattern(str: String?)

    external fun nAddHalftoneLayer(i: Int, i2: Int, i3: Int, z: Boolean)

    external fun nAddLayer()

    external fun nAddLayer1()

    external fun nAddLayer8(i: Int)

    external fun nAddLayerFolder()

    external fun nAddLayerText()

    external fun nAddMaterial(bitmap: Bitmap?, i: Int, i2: Int)

    external fun nAddMaterialKoma(i: Int, i2: Int, i3: Int, i4: Int, i5: Int, z: Boolean)

    external fun nAddSelectedLayer(i: Int)

    external fun nAnts(bitmap: Bitmap?)

    external fun nBeginSelectTransform(i: Int)

    external fun nBrushing(): Boolean

    external fun nCalcCmSize(d: Double, i: Int, i2: Int): Double

    external fun nCalcInchSize(d: Double, i: Int, i2: Int): Double

    external fun nCalcPixelSize(d: Double, i: Int, i2: Int): Int

    external fun nCanAddText(i: Int, i2: Int): Boolean

    external fun nCanLayerAdd(): Boolean

    external fun nCanLayerLower(): Boolean

    external fun nCanLayerRemove(): Boolean

    external fun nCanLayerUpper(): Boolean

    external fun nCanMultiBrushMdp(str: String?, str2: String?, i: Int): Boolean

    external fun nCanOpenMDP(str: String?): Boolean

    external fun nCanRedo(): Boolean

    external fun nCanUndo(): Boolean

    external fun nCancelBrush(bitmap: Bitmap?)

    external fun nCancelSelectTransform(): Boolean

    external fun nCanvasResize(i: Int, i2: Int, i3: Int)

    external fun nCanvasResolution(i: Int, i2: Int)

    external fun nCanvasRotate(i: Int)

    external fun nCanvasTrim()

    external fun nChanged(): Boolean

    external fun nClearArtworkInfo()

    external fun nClearBrushOption()

    external fun nClearDirty()

    external fun nClearEditOffset()

    external fun nClearLayer()

    external fun nClearRotMirror(bitmap: Bitmap?)

    external fun nClientToImage(x: Float, y: Float): FloatArray?

    external fun nClientToImageView(x: Float, y: Float): FloatArray?

    external fun nCurveBarHeight(): Int

    external fun nDeleteLayer()

    external fun nDeleteSnap(i: Int)

    external fun nDoneSelectTransform()

    external fun nEndFilterMode()

    external fun nEntrenchEffect()

    external fun nEventDivFrameRectN(i: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int)

    external fun nFillBrush(
        dArr: DoubleArray?,
        dArr2: DoubleArray?,
        i: Int,
        bitmap: Bitmap?,
        z: Boolean
    )

    external fun nFillSelectBorder(i: Int, i2: Int): Boolean

    external fun nFilterChromaticAberration(i: Int, i2: Int)

    external fun nFilterChromaticAberrationPreview(i: Int, i2: Int)

    external fun nFilterGauss(f: Float)

    external fun nFilterGaussPreview(f: Float)

    external fun nFilterGradMap(i: Int)

    external fun nFilterGradMapPreview(i: Int)

    external fun nFilterHue(i: Int, i2: Int, i3: Int)

    external fun nFilterHuePreview(i: Int, i2: Int, i3: Int)

    external fun nFilterLine(i: Int, i2: Int, i3: Int)

    external fun nFilterLinePreview(i: Int, i2: Int, i3: Int)

    external fun nFilterMono()

    external fun nFilterMonoPreview()

    external fun nFilterMosaic(i: Int)

    external fun nFilterMosaicPreview(i: Int)

    external fun nFilterTone(iArr: IntArray?, iArr2: IntArray?, iArr3: IntArray?)

    external fun nFilterTonePreview(iArr: IntArray?, iArr2: IntArray?, iArr3: IntArray?)

    external fun nFilterUnsharpMask(d: Double, i: Int)

    external fun nFilterUnsharpMaskPreview(d: Double, i: Int)

    external fun nFinishPolygon(bitmap: Bitmap?)

    external fun nGetActiveLayer(): Int

    external fun nGetActiveLayerThumb(bitmap: Bitmap?)

    external fun nGetActiveLayerThumbChromaticAberration(bitmap: Bitmap?)

    external fun nGetActiveLayerThumbGauss(bitmap: Bitmap?)

    external fun nGetActiveLayerThumbGradMap(bitmap: Bitmap?, i: Int)

    external fun nGetActiveLayerThumbHue(bitmap: Bitmap?)

    external fun nGetActiveLayerThumbInverse(bitmap: Bitmap?)

    external fun nGetActiveLayerThumbLineArt(bitmap: Bitmap?)

    external fun nGetActiveLayerThumbMono(bitmap: Bitmap?)

    external fun nGetActiveLayerThumbMosaic(bitmap: Bitmap?)

    external fun nGetActiveLayerThumbSize(): IntArray?

    external fun nGetActiveLayerThumbUnsharpMask(bitmap: Bitmap?)

    external fun nGetBrushIriNuki(): Boolean

    external fun nGetBrushPreview(bitmap: Bitmap?)

    external fun nGetBrushScriptOptionCount(): Int

    external fun nGetBrushScriptOptionName(i: Int): String?

    external fun nGetBrushScriptOptionValue(i: Int, i2: Int): Int

    external fun nGetBrushSize(): Float

    external fun nGetCanvasSize(str: String?): IntArray?

    external fun nGetComicGuideInside(): DoubleArray?

    external fun nGetComicGuideIsSpread(): Boolean

    external fun nGetComicGuideNuritashi(): Double

    external fun nGetComicGuideOutside(): DoubleArray?

    external fun nGetComicGuideSpineWidth(): Double

    external fun nGetComicGuideVisible(): Boolean

    external fun nGetDefaultBGColor(): IntArray?

    external fun nGetDirty(): IntArray?

    external fun nGetDpi(): Int

    external fun nGetDropBoxApiKey(): String?

    external fun nGetEffectType(): Int

    external fun nGetEffectTypeIndex(i: Int): Int

    external fun nGetEffectWcAlpha(): Int

    external fun nGetEffectWcPx(): Int

    external fun nGetFillRoundRate(): Float

    external fun nGetFont(): String?

    external fun nGetGradPatternCount(): Int

    external fun nGetGradPatternImage(bitmap: Bitmap?, i: Int): String?

    external fun nGetHalftoneDensityType(): Int

    external fun nGetHalftoneDensityValue(): Int

    external fun nGetHalftoneLine(): Int

    external fun nGetHalftoneType(): Int

    external fun nGetHalftoneTypeIndex(i: Int): Int

    external fun nGetLayerAlpha(i: Int): Int

    external fun nGetLayerBlend(i: Int): Int

    external fun nGetLayerClipping(i: Int): Boolean

    external fun nGetLayerColor(i: Int): Int

    external fun nGetLayerIndent(i: Int): Int

    external fun nGetLayerLockAlpha(i: Int): Boolean

    external fun nGetLayerMaskType(i: Int): Int

    external fun nGetLayerName(i: Int): String?

    external fun nGetLayerNum(): Int

    external fun nGetLayerOpened(i: Int): Boolean

    external fun nGetLayerThumb(i: Int, bitmap: Bitmap?)

    external fun nGetLayerThumbSize(i: Int): IntArray?

    external fun nGetLayerType(i: Int): Int

    external fun nGetLayerVisible(i: Int): Boolean

    external fun nGetMaterialColor(): Int

    external fun nGetMaterialWidth(): Int

    external fun nGetMoveActive(): Boolean

    external fun nGetOverlayItemNum(i: Int): Int

    external fun nGetPlaneFigure(): Int

    external fun nGetSavedSnapMode(i: Int): Int

    external fun nGetScriptMarkMode(): Boolean

    external fun nGetScriptMarkPoint(): IntArray?

    external fun nGetSelectRoundRate(): Float

    external fun nGetSelectedLayers(): IntArray?

    external fun nGetSnapCount(): Int

    external fun nGetSnapMode(): Int

    external fun nGetSnapName(i: Int): String?

    external fun nGetStrokePointsX(dArr: DoubleArray?, dArr2: DoubleArray?): DoubleArray?

    external fun nGetStrokePointsY(dArr: DoubleArray?, dArr2: DoubleArray?): DoubleArray?

    external fun nGetTextAA(): Boolean

    external fun nGetTextAlign(): Int

    external fun nGetTextBold(): Boolean

    external fun nGetTextCharMargin(): Double

    external fun nGetTextCharSize(): Double

    external fun nGetTextColor(): Int

    external fun nGetTextColumn(): Boolean

    external fun nGetTextEdgeColor(): Int

    external fun nGetTextEdgeRound(): Boolean

    external fun nGetTextEdgeWidth(): Double

    external fun nGetTextItalic(): Boolean

    external fun nGetTextLineMargin(): Double

    external fun nGetTextOffset(): IntArray?

    external fun nGetTextOffsetRightBottom(): IntArray?

    external fun nGetTextRotate(): Double

    external fun nGetTextString(): String?

    external fun nGetThumbMDP(bitmap: Bitmap?)

    external fun nGetThumbMDPFull(str: String?, z: Boolean, i: Int, bitmap: Bitmap?)

    external fun nGetThumbPSD(bitmap: Bitmap?)

    external fun nGetThumbSizeMDP(str: String?): IntArray?

    external fun nGetThumbSizeMDPFull(str: String?): IntArray?

    external fun nGetThumbSizePSD(str: String?): IntArray?

    external fun nGetTmpFolder(): String?

    external fun nGetViewCache(bitmap: Bitmap?)

    external fun nGetViewCacheSize(i: Int, i2: Int): FloatArray?

    external fun nGetViewRotate(): Float

    external fun nHeight(): Int

    external fun nImageToClient(f: Float, f2: Float): FloatArray?

    external fun nImageToClientView(f: Float, f2: Float): FloatArray?

    external fun nInMirror(): Boolean

    external fun nInitSnap()

    external fun nIsCheckerBG(): Boolean

    external fun nIsControlKeyDown(): Boolean

    external fun nIsCurvePointMode(): Boolean

    external fun nIsKomaExists(i: Int): Boolean

    external fun nIsLayerDraft(i: Int): Boolean

    external fun nIsLayerLocked(i: Int): Boolean

    external fun nIsPolygonPointMode(): Boolean

    external fun nIsPolylinePointMode(): Boolean

    external fun nIsSettingClicked(f: Float, f2: Float, i: Int): Boolean

    external fun nKeyDownControl(bitmap: Bitmap?)

    external fun nKeyDownControlEvent(bitmap: Bitmap?)

    external fun nKeyDownShift(bitmap: Bitmap?)

    external fun nKeyDownShiftEvent(bitmap: Bitmap?)

    external fun nKeyUpControl(bitmap: Bitmap?)

    external fun nKeyUpControlEvent(bitmap: Bitmap?)

    external fun nKeyUpShift(bitmap: Bitmap?)

    external fun nKeyUpShiftEvent(bitmap: Bitmap?)

    external fun nLayerClippable(i: Int): Boolean

    external fun nLayerCombineInFolder()

    external fun nLayerConvert1(): Boolean

    external fun nLayerConvert32(): Boolean

    external fun nLayerConvert8(): Boolean

    external fun nLayerDuplicate()

    external fun nLayerFlip(i: Int)

    external fun nLayerLower()

    external fun nLayerMerge()

    external fun nLayerMergeFolder()

    external fun nLayerMoveInFolder(i: Int): Int

    external fun nLayerMoveMulti(i: Int, i2: Int, z: Boolean): Int

    external fun nLayerMoveSingle(i: Int, i2: Int, z: Boolean): Int

    external fun nLayerUpper()

    external fun nLoadSnap(i: Int)

    external fun nMaterialPasteCancel()

    external fun nMaterialPasteFinish()

    external fun nMaterialPasteMoveTo(f: Float, f2: Float, z: Boolean)

    external fun nMaterialPasteRotTo(d: Double, z: Boolean)

    external fun nMaterialPasteStart(f: Float, f2: Float)

    external fun nMaterialPasteZoomTo(d: Double, z: Boolean)

    external fun nMergeMaterial()

    external fun nNew(i: Int, i2: Int)

    external fun nNew1(i: Int, i2: Int)

    external fun nNew8(i: Int, i2: Int)

    external fun nOpenBitmap(bitmap: Bitmap?)

    external fun nOpenMDP(str: String?): Boolean

    external fun nOpenPSD(str: String?): Boolean

    external fun nPaint(bitmap: Bitmap?)

    external fun nPushEmptyUndo()

    external fun nQuickMask(): Boolean

    external fun nRasterize(bitmap: Bitmap?, z: Boolean)

    external fun nRedo(bitmap: Bitmap?): Int

    external fun nRefreshAnts(i: Int, i2: Int)

    external fun nResetMeshTransForm()

    external fun nResize(i: Int, i2: Int)

    external fun nSaveMDP(str: String?): Boolean

    external fun nSaveMDPWithOption(str: String?, z: Boolean): Boolean

    external fun nSavePNG(str: String?, z: Boolean): Boolean

    external fun nSavePSD(str: String?): Boolean

    external fun nSaveSnap(str: String?)

    external fun nScriptMarkPoint(f: Float, f2: Float)

    external fun nSelectAll()

    external fun nSelectClear()

    external fun nSelectCutCopyPaste(i: Int, bitmap: Bitmap?)

    external fun nSelectDrawingAlpha(i: Int)

    external fun nSelectExists(): Boolean

    external fun nSelectInverse()

    external fun nSelectMoving(): Boolean

    external fun nSelectTransforming(): Boolean

    external fun nSetActiveLayer(i: Int)

    external fun nSetAnchorRange(i: Int)

    external fun nSetArtworkInfo(str: String?, i: Int, i2: Int, i3: Int, i4: Int)

    external fun nSetBrushAA(z: Boolean)

    external fun nSetBrushBitmap(bitmap: Bitmap?)

    external fun nSetBrushCorrection(i: Int)

    external fun nSetBrushDraw(i: Int)

    external fun nSetBrushInterpolate(z: Boolean)

    external fun nSetBrushIriNuki(z: Boolean)

    external fun nSetBrushMdp(str: String?, str2: String?)

    external fun nSetBrushMinR(f: Float)

    external fun nSetBrushMode(i: Int)

    external fun nSetBrushOpaque(f: Float)

    external fun nSetBrushOption(i: Int, i2: Int)

    external fun nSetBrushPressTrans(z: Boolean)

    external fun nSetBrushPressWidth(z: Boolean)

    external fun nSetBrushScript(str: String?)

    external fun nSetBrushScriptParams(str: String?, z: Boolean)

    external fun nSetBrushSize(f: Float)

    external fun nSetBrushSnapEllipse(f: Float, f2: Float)

    external fun nSetBrushSnapPara(f: Float, f2: Float, f3: Float, f4: Float)

    external fun nSetBrushSnapRadial(f: Float, f2: Float)

    external fun nSetBrushSnapVanish1(f: Float, f2: Float, f3: Float, f4: Float)

    external fun nSetBrushSnapVanish2(f: Float, f2: Float, f3: Float, f4: Float)

    external fun nSetBrushSoftEdge(z: Boolean)

    external fun nSetBucketAA(z: Boolean)

    external fun nSetBucketExtend(i: Int)

    external fun nSetCheckerBG(z: Boolean)

    external fun nSetColor(i: Int, i2: Int, i3: Int)

    external fun nSetColorBG(i: Int, i2: Int, i3: Int)

    external fun nSetComicGuide(
        outSideWidth: Double,
        outSideHeight: Double,
        inSideWidth: Double,
        inSideHeight: Double,
        bleed: Double,
        bookCoverWidth: Double,
        dpi: Int,
        i2: Int, // 4
        isSpread: Boolean
    )

    external fun nSetComicGuideVisible(z: Boolean)

    external fun nSetCurveBarHeight(i: Int)

    external fun nSetCurvePointMode(z: Boolean)

    external fun nSetDefaultBGColor(red: Int, green: Int, blue: Int, alpha: Boolean)

    external fun nSetDivHeightMargin(i: Int)

    external fun nSetDivHeightUnit(i: Int)

    external fun nSetDivWidthMargin(i: Int)

    external fun nSetDivWidthUnit(i: Int)

    external fun nSetDpi(i: Int)

    external fun nSetEffectType(i: Int)

    external fun nSetEffectWcAlpha(i: Int)

    external fun nSetEffectWcPx(i: Int)

    external fun nSetEllipseSnapButtons(
        bitmap: Bitmap?,
        bitmap2: Bitmap?,
        bitmap3: Bitmap?,
        bitmap4: Bitmap?
    )

    external fun nSetFillAA(z: Boolean)

    external fun nSetFillAlpha(i: Int)

    external fun nSetFillBucketLeak(i: Int)

    external fun nSetFillBucketType(i: Int)

    external fun nSetFillMode(i: Int)

    external fun nSetFillRound(z: Boolean, f: Float)

    external fun nSetFilterInverse(z: Boolean)

    external fun nSetGradFill(i: Int)

    external fun nSetGradMode(i: Int)

    external fun nSetGridEnable(z: Boolean)

    external fun nSetHalftoneType(i: Int, i2: Int, i3: Int, z: Boolean)

    external fun nSetIdentity(str: String?)

    external fun nSetLayerAlpha(i: Int, i2: Int)

    external fun nSetLayerBlend(i: Int, i2: Int)

    external fun nSetLayerClipping(i: Int, z: Boolean)

    external fun nSetLayerColor(i: Int, i2: Int)

    external fun nSetLayerDraft(i: Int, z: Boolean): Boolean

    external fun nSetLayerLockAlpha(i: Int, z: Boolean)

    external fun nSetLayerLocked(i: Int, z: Boolean): Boolean

    external fun nSetLayerMaskType(i: Int, z: Boolean, i2: Int)

    external fun nSetLayerName(str: String?)

    external fun nSetLayerOpened(i: Int, z: Boolean)

    external fun nSetLayerVisible(i: Int, z: Boolean)

    external fun nSetMaterialImage32(bitmap: Bitmap?, str: String?, z: Boolean)

    external fun nSetMaterialOpIcon(i: Int, bitmap: Bitmap?)

    external fun nSetMaterialOpIconDensity(d: Double)

    external fun nSetMaterialProp(i: Int, i2: Int)

    external fun nSetMeshTransFormHLink(z: Boolean)

    external fun nSetMeshTransFormVLink(z: Boolean)

    external fun nSetMeshTransParam(i: Int, i2: Int)

    external fun nSetMoveActive(z: Boolean)

    external fun nSetMultiThread(z: Boolean)

    external fun nSetPixelGrid(z: Boolean)

    external fun nSetPlaneFigure(i: Int)

    external fun nSetPolygonPointMode(z: Boolean)

    external fun nSetPolylinePointMode(z: Boolean)

    external fun nSetPressureGamma(f: Float)

    external fun nSetQuickMask(z: Boolean)

    external fun nSetQuickMaskMode(i: Int)

    external fun nSetScriptMarkMode(z: Boolean)

    external fun nSetSelectAA(z: Boolean)

    external fun nSetSelectMode(i: Int)

    external fun nSetSelectPenAA(z: Boolean)

    external fun nSetSelectRound(z: Boolean, f: Float)

    external fun nSetSelectWandAA(z: Boolean)

    external fun nSetSelectWandType(i: Int)

    external fun nSetSnapMode(i: Int)

    external fun nSetText(
        bitmap: Bitmap?,
        strArr: Array<String?>?,
        i: Int,
        i2: Int,
        z: Boolean,
        z2: Boolean,
        z3: Boolean,
        z4: Boolean,
        d: Double,
        d2: Double,
        d3: Double,
        str: String?,
        i3: Int,
        d4: Double,
        d5: Double,
        z5: Boolean
    )

    external fun nSetTextOffset(i: Int, i2: Int)

    external fun nSetTmpFolder(str: String?)

    external fun nSetTool(i: Int)

    external fun nSetTransformParse(z: Boolean)

    external fun nSetViewRotate(bitmap: Bitmap?, f: Float)

    external fun nSetWandExtend(i: Int)

    external fun nSetWandLeak(i: Int)

    external fun nSpoitColor(i: Int, i2: Int): IntArray?

    external fun nTouchBegin(bitmap: Bitmap?, f: Float, f2: Float, f3: Float)

    external fun nTouchEnd(bitmap: Bitmap?, f: Float, f2: Float, f3: Float)

    external fun nTouchMove(bitmap: Bitmap?, f: Float, f2: Float, f3: Float)

    external fun nTransformAnchor(): IntArray?

    external fun nTransformView(bitmap: Bitmap?, f: Float, f2: Float, f3: Float, f4: Float)

    external fun nUndo(bitmap: Bitmap?): Int

    external fun nUseScriptMark(): Boolean

    external fun nViewMove(f: Float, f2: Float)

    external fun nViewReverse(bitmap: Bitmap?)

    external fun nViewRot(): Float

    external fun nViewZoom(): Float

    external fun nWidth(): Int

    external fun nZoomFit()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MeiPaintTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting2(canvasBitmap)
                }
            }
        }
        testNative()
        testInitCanvas()
    }

    fun testNative() {
        // Call the native method and get the result
        val result = test()

        // Log the result
        Log.d("MeiPaint", "Native method returned: $result")
    }

    fun testInitCanvas() {

        val apiKey = nGetDropBoxApiKey();
        Log.d("MeiPaint", "Native API KEY returned: $apiKey")


        initialize(1000, 1414, 2);
        nSetCurveBarHeight( 2 * 40)
        val nCurveBarHeight = (nCurveBarHeight().toDouble() * 0.9).toInt()

        canvasBitmap = Bitmap.createBitmap(nWidth(), nHeight(), Bitmap.Config.ARGB_8888)

//        nSetEllipseSnapButtons()
        nClearArtworkInfo();
        nNew(1000, 1414)
        nSetDpi(72)

//        nSetComicGuideVisible(true);
//        nSetComicGuide()
        nSetCheckerBG(true);
        nSetDefaultBGColor(10, 205, 20, true);

        nSetColor(10,200,20)
        nSetColorBG(10,200,20)

        nClientToImageView(0f , 0f)

        val iriNuki = nGetBrushIriNuki() // Iri = entrance, Nuki = exit, stroke pressure effect
        Log.d("MeiPaint", "Native iriNuki returned: $iriNuki")


        val width = nWidth()
        Log.d("MeiPaint", "Native width returned: $width")


        nGetViewCache(canvasBitmap);
        Log.d("MeiPaint", "Native canvasBitmap returned: $canvasBitmap")
//        nPaint(canvasBitmap);

        // nGetViewCache
        // nPaint

        val createBitmap = Bitmap.createBitmap(
            nWidth(),
            nHeight(),
            Bitmap.Config.ARGB_8888
        )

        nRasterize(createBitmap, false);

        val filePath = Environment.getExternalStorageDirectory().path
        val fileName = "your_image.jpg"

        try {
            FileOutputStream(File(filePath, fileName)).use { fileOutputStream ->
                createBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Log.d("MeiPaint", "Canvas saved to JPG as : $filePath/$fileName")


    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Greeting2(canvasBitmap: Bitmap, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val action: MutableState<Pair<Boolean, Pair<Float, Float>>?> = remember { mutableStateOf(null) }
    val path = Path()

    val triggerList = mutableListOf<Pair<Boolean, Pair<Float, Float>>>()
    val collectList = mutableListOf<Pair<Boolean, Pair<Float, Float>>>()

//    val motionEvent = viewModel.motionEvent

    val baseImageBitmap: ImageBitmap? = null

    val imageBitmap: ImageBitmap = remember { canvasBitmap.asImageBitmap() }

    val drawModifier = Modifier
        .drawBehind {
            if (imageBitmap != null) {
                drawImage(
                    image = imageBitmap,
                    topLeft = Offset.Zero,
                )
                Log.d("CanvasPage", "draw background image")
            } else {
                drawRect(
                    color = Color.White,
                    topLeft = Offset.Zero,
                    size = size
                )
                Log.d("CanvasPage", "draw white rectangle")
            }
        }
        .background(Color.LightGray)
        .padding(8.dp)
        .shadow(1.dp)
        .fillMaxWidth()

    Canvas(
        modifier = drawModifier
    ) { // https://developer.android.com/jetpack/compose/graphics/images/custompainter
        Log.d("CanvasPage", "canvas width: ${size.width}, height: ${size.height}")

//        when (motionEvent.value) {
//
//        }

        with(drawContext.canvas.nativeCanvas) {
            val checkPoint = saveLayer(null, null)
            drawBitmap(canvasBitmap, 0f, 0f, null)
        }



    }
}
