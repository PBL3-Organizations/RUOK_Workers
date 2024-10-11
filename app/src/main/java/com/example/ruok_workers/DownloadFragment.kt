package com.example.ruok_workers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.ruok_workers.databinding.FragmentDownloadBinding
import com.getkeepsafe.relinker.BuildConfig
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFClientAnchor
import org.apache.poi.xssf.usermodel.XSSFDrawing
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class DownloadFragment : Fragment() {

    private lateinit var binding: FragmentDownloadBinding
    private lateinit var dbManager: DBManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDownloadBinding.inflate(inflater, container, false)

        // btnDownloadCounting 버튼 클릭 시 엑셀 파일 생성 및 다운로드
        binding.btnDownloadCounting.setOnClickListener {
            val countingRecords = getCountingRecords() // DB에서 카운팅 데이터 가져오기
            val countingAreas = getCountingAreas() // DB에서 구역 이름 가져오기

            // 엑셀 파일 생성
            val excelFile = createExcelFile(requireContext(), countingRecords, countingAreas)

            // 파일 공유 또는 다운로드 처리
            val uri = FileProvider.getUriForFile(
                requireContext(),
                "com.example.ruok_workers.provider", // 패키지 이름을 직접 입력
                excelFile
            )

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            startActivity(Intent.createChooser(intent, "엑셀 파일 다운로드"))
        }

        // btnDownloadConsultation 버튼 클릭 시 엑셀 파일 생성 및 다운로드
        binding.btnDownloadConsultation.setOnClickListener {
            val consultationRecords = getConsultationRecords() // DB에서 상담 데이터 가져오기
            val excelFile = createConsultationExcelFile(requireContext(), consultationRecords)

            val uri = FileProvider.getUriForFile(
                requireContext(),
                "com.example.ruok_workers.provider", // 패키지 이름을 직접 입력
                excelFile
            )

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            startActivity(Intent.createChooser(intent, "엑셀 파일 다운로드"))
        }

        return binding.root
    }

    // DB에서 데이터를 가져오는 함수 (구역 이름 및 남, 여, 소계 값 함께 가져오기)
    private fun getCountingRecords(): List<CountingRecord> {
        val countingRecords = mutableListOf<CountingRecord>()
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        val sqlitedb = dbManager.readableDatabase

        // 쿼리 수정: counting_record와 counting_area를 조인하여 구역 이름과 카운팅 값을 함께 가져오기
        val query = """
            SELECT cl.cl_date, ca.ca_name, cr.cr_male, cr.cr_female, cr.cr_sum
            FROM counting_record cr
            JOIN counting_area ca ON cr.ca_num = ca.ca_num
            JOIN counting_list cl ON cr.cl_date = cl.cl_date
        """
        val cursor = sqlitedb.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val clDate = cursor.getString(cursor.getColumnIndexOrThrow("cl_date"))
            val areaName = cursor.getString(cursor.getColumnIndexOrThrow("ca_name"))
            val male = cursor.getInt(cursor.getColumnIndexOrThrow("cr_male"))
            val female = cursor.getInt(cursor.getColumnIndexOrThrow("cr_female"))
            val sum = cursor.getInt(cursor.getColumnIndexOrThrow("cr_sum"))

            countingRecords.add(CountingRecord(clDate, male, female, sum, areaName))
        }

        cursor.close()
        sqlitedb.close()
        return countingRecords
    }

    // DB에서 counting_area(ca_name)를 가져오는 함수
    private fun getCountingAreas(): List<String> {
        val countingAreas = mutableListOf<String>()
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        val sqlitedb = dbManager.readableDatabase

        val query = "SELECT ca_name FROM counting_area"
        val cursor = sqlitedb.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val areaName = cursor.getString(cursor.getColumnIndexOrThrow("ca_name"))
            countingAreas.add(areaName)
        }

        cursor.close()
        sqlitedb.close()
        return countingAreas
    }

    // 엑셀 파일 생성 함수 (정렬 관련 코드 제거)
    private fun createExcelFile(context: android.content.Context, countingRecords: List<CountingRecord>, countingAreas: List<String>): File {
        val workbook: Workbook = XSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("CountingReport")

        // 보고일자 및 구분 셀 병합 (보고일자: A2~A3, 구분: B2~B3)
        sheet.addMergedRegion(CellRangeAddress(1, 2, 0, 0))
        sheet.addMergedRegion(CellRangeAddress(1, 2, 1, 1))

        // 1행 (보고일자, 구분)
        var row: Row = sheet.createRow(1)
        row.createCell(0).setCellValue("보고일자")
        row.createCell(1).setCellValue("구분")

        // C열부터 구역명 추가
        for ((i, areaName) in countingAreas.withIndex()) {
            sheet.addMergedRegion(CellRangeAddress(1, 1, 2 + i * 3, 4 + i * 3))
            row.createCell(2 + i * 3).setCellValue(areaName)
        }

        // 2행 (남, 여, 소계)
        row = sheet.createRow(2)
        for (i in countingAreas.indices) {
            row.createCell(2 + i * 3).setCellValue("남")
            row.createCell(3 + i * 3).setCellValue("여")
            row.createCell(4 + i * 3).setCellValue("소계")
        }

        // 데이터를 보고일자별로 그룹화
        val groupedRecords = countingRecords.groupBy { it.date }

        // 3행부터 보고일자별로 데이터 삽입
        var rowIndex = 3
        for ((date, records) in groupedRecords) {
            row = sheet.createRow(rowIndex)
            row.createCell(0).setCellValue(date.toDouble())
            row.createCell(1).setCellValue("야간")

            // 각 구역별 남, 여, 소계 값을 0으로 초기화
            val areaData = mutableMapOf<String, Triple<Int, Int, Int>>()
            for (area in countingAreas) {
                areaData[area] = Triple(0, 0, 0)
            }

            // 보고일자에 해당하는 레코드를 순회하며 구역별 데이터를 집계
            for (record in records) {
                if (record.areaName in areaData) {
                    areaData[record.areaName] = Triple(record.male, record.female, record.sum)
                }
            }

            // 집계된 데이터를 엑셀에 삽입
            for ((index, areaName) in countingAreas.withIndex()) {
                val data = areaData[areaName]
                if (data != null) {
                    row.createCell(2 + index * 3).setCellValue(data.first.toDouble()) // 남
                    row.createCell(3 + index * 3).setCellValue(data.second.toDouble()) // 여
                    row.createCell(4 + index * 3).setCellValue(data.third.toDouble()) // 소계
                }
            }

            rowIndex++ // 다음 행으로 이동
        }

        // 모든 열 자동 너비 조정
        for (i in 0 until (2 + countingAreas.size * 3)) {
            sheet.setColumnWidth(i, 4000)
        }

        // 파일 저장 경로 설정
        val file = File(context.getExternalFilesDir(null), "아웃리치일일보고.xlsx")
        val outputStream = FileOutputStream(file)
        workbook.write(outputStream)
        outputStream.close()
        workbook.close()

        return file
    }

    // 상담 기록을 DB에서 가져오는 함수
    private fun getConsultationRecords(): List<ConsultationRecord> {
        val consultationRecords = mutableListOf<ConsultationRecord>()
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        val sqlitedb = dbManager.readableDatabase

        // 필요한 데이터를 조인해서 가져오는 SQL 쿼리
        val query = """
            SELECT h.h_num, h.h_name, h.h_birth, h.h_photo,
                   c.m_num, c.c_time, c.c_unusual, c.c_measure, c.c_content, m.m_name 
            FROM homeless h 
            JOIN consultation c ON h.h_num = c.h_num 
            JOIN member m ON c.m_num = m.m_num 
            ORDER BY h.h_num, c.c_time DESC
        """
        val cursor = sqlitedb.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val hNum = cursor.getInt(cursor.getColumnIndexOrThrow("h_num"))
            val hName = cursor.getString(cursor.getColumnIndexOrThrow("h_name"))
            val hBirth = cursor.getString(cursor.getColumnIndexOrThrow("h_birth"))
            val hPhoto = cursor.getString(cursor.getColumnIndexOrThrow("h_photo")) // 노숙인 사진 경로 추가
            val mName = cursor.getString(cursor.getColumnIndexOrThrow("m_name"))
            val cTime = cursor.getString(cursor.getColumnIndexOrThrow("c_time"))
            val cUnusual = cursor.getString(cursor.getColumnIndexOrThrow("c_unusual"))
            val cMeasure = cursor.getString(cursor.getColumnIndexOrThrow("c_measure"))
            val cContent = cursor.getString(cursor.getColumnIndexOrThrow("c_content"))

            consultationRecords.add(
                ConsultationRecord(hNum, hName, hBirth, hPhoto, mName, cTime, cUnusual, cMeasure, cContent)
            )
        }

        cursor.close()
        sqlitedb.close()
        return consultationRecords
    }

    // 상담 기록 엑셀 파일 생성 함수
    private fun createConsultationExcelFile(context: Context, records: List<ConsultationRecord>): File {
        val workbook: Workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("OutreachConsultation")

        // 1행 (A1부터 H1까지 병합 후 제목 설정)
        sheet.addMergedRegion(CellRangeAddress(0, 0, 0, 7))
        val row = sheet.createRow(0)
        row.createCell(0).setCellValue("아웃리치 위기대상 노숙인")

        // 2행 (열 제목 설정)
        val headerRow = sheet.createRow(1)
        headerRow.createCell(0).setCellValue("연번")
        headerRow.createCell(1).setCellValue("사진")
        headerRow.createCell(2).setCellValue("성명")
        headerRow.createCell(3).setCellValue("생년월일")
        headerRow.createCell(4).setCellValue("담당")
        headerRow.createCell(5).setCellValue("수급/건강상태")
        headerRow.createCell(6).setCellValue("상담내역")
        headerRow.createCell(7).setCellValue("조치내용")

        val drawing: XSSFDrawing = sheet.createDrawingPatriarch() as XSSFDrawing

        // 데이터 행 추가
        for ((index, record) in records.withIndex()) {
            val dataRow = sheet.createRow(index + 2) // 3행부터 데이터 삽입

            dataRow.createCell(0).setCellValue(index + 1.0) // 연번
            dataRow.createCell(2).setCellValue(record.hName) // 성명
            dataRow.createCell(3).setCellValue(record.hBirth) // 생년월일
            dataRow.createCell(4).setCellValue(record.mName) // 담당
            dataRow.createCell(5).setCellValue(record.cUnusual) // 수급/건강상태
            dataRow.createCell(6).setCellValue(record.cContent) // 상담내역
            dataRow.createCell(7).setCellValue(record.cMeasure) // 조치내용

            val photoPath = record.hPhoto
            if (photoPath.startsWith("/")) {
                // 내부 저장소 경로에서 이미지 불러오기
                val file = File(photoPath)
                if (file.exists()) {
                    val bitmap: Bitmap = BitmapFactory.decodeFile(file.absolutePath)

                    // Bitmap을 byte array로 변환
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val bytes = workbook.addPicture(stream.toByteArray(), Workbook.PICTURE_TYPE_PNG)
                    stream.close()

                    // 이미지 삽입 (resize 없이 직접 위치 및 크기 지정)
                    val anchor = XSSFClientAnchor(0, 0, 1023, 255, 1, index + 2, 2, index + 3)
                    anchor.anchorType = ClientAnchor.AnchorType.MOVE_AND_RESIZE

                    drawing.createPicture(anchor, bytes)
                } else {
                    Log.e("ExcelCreation", "내부 저장소 이미지가 존재하지 않음: $photoPath")
                    dataRow.createCell(1).setCellValue("")
                }
            } else {
                // drawable에서 이미지 불러오기
                val imageName = photoPath.substringBefore('.') // 확장자 제거
                val imagePath = context.resources.getIdentifier(imageName, "drawable", context.packageName)

                if (imagePath != 0) { // 이미지 리소스가 존재하는지 확인
                    val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, imagePath)

                    // Bitmap을 byte array로 변환
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val bytes = workbook.addPicture(stream.toByteArray(), Workbook.PICTURE_TYPE_PNG)
                    stream.close()

                    // 이미지 삽입 (resize 없이 직접 위치 및 크기 지정)
                    val anchor = XSSFClientAnchor(0, 0, 1023, 255, 1, index + 2, 2, index + 3)
                    anchor.anchorType = ClientAnchor.AnchorType.MOVE_AND_RESIZE

                    drawing.createPicture(anchor, bytes)
                } else {
                    Log.e("ExcelCreation", "drawable 이미지가 존재하지 않음: $imageName")
                    dataRow.createCell(1).setCellValue("")
                }
            }
        }

//            // 이미지 파일 경로 (drawable에서 가져오기)
//            val imageName = record.hPhoto.substringBefore('.') // 확장자 제거
//            val imagePath = context.resources.getIdentifier(imageName, "drawable", context.packageName)
//
//            // 로그 출력
//            Log.d("ExcelCreation", "이미지 리소스 ID: $imagePath")
//
//            if (imagePath != 0) { // 이미지 리소스가 존재하는지 확인
//                Log.d("ExcelCreation", "이미지 리소스 존재: $imagePath")
//
//                val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, imagePath)
//
//                // Bitmap을 byte array로 변환
//                val stream = ByteArrayOutputStream()
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
//                val bytes = workbook.addPicture(stream.toByteArray(), Workbook.PICTURE_TYPE_PNG)
//                stream.close()
//
//                // 이미지 삽입 (resize 없이 직접 위치 및 크기 지정)
//                val anchor = XSSFClientAnchor(0, 0, 1023, 255, 1, index + 2, 2, index + 3) // 크기 조정 없이 앵커 설정
//                anchor.anchorType = ClientAnchor.AnchorType.MOVE_AND_RESIZE
//
//                drawing.createPicture(anchor, bytes)
//            } else {
//                Log.e("ExcelCreation", "이미지 리소스가 존재하지 않음: $imageName")
//                // 이미지가 없는 경우 공란으로 처리
//                dataRow.createCell(1).setCellValue("")
//            }
//        }

        // 열 너비 조정 (cm 단위)
        sheet.setColumnWidth(1, (13 * 256)) // B열 너비 3.3 cm -> 약 13 character width
        sheet.setColumnWidth(5, (20 * 256)) // F열 너비 5 cm -> 약 20 character width
        sheet.setColumnWidth(6, (60 * 256)) // G열 너비 15 cm -> 약 60 character width
        sheet.setColumnWidth(7, (60 * 256)) // H열 너비 15 cm -> 약 60 character width

        // 3행부터 행 높이 조정 (4.3 cm -> 약 122 points)
        for (i in 2 until sheet.physicalNumberOfRows) {
            sheet.getRow(i).heightInPoints = 122f
        }

        // 파일 저장 경로 설정
        val file = File(context.getExternalFilesDir(null), "아웃리치 위기대상 노숙인.xlsx")
        val outputStream = FileOutputStream(file)
        workbook.write(outputStream)
        outputStream.close()
        workbook.close()

        return file
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 뒤로가기 버튼을 눌렀을 때 백스택을 비우고 DashboardFragment로 이동
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // 백스택을 전부 비움
                    parentFragmentManager.popBackStack(
                        null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    // DashboardFragment로 이동
                    val parentActivity = activity as DashboardActivity
                    parentActivity.setFragment(DashboardFragment())
                }
            }
        )
    }
}

